package com.store.service.filter;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.edm.EdmEnumType;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.commons.core.edm.primitivetype.EdmString;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourcePrimitiveProperty;
import org.apache.olingo.server.api.uri.queryoption.apply.AggregateExpression;
import org.apache.olingo.server.api.uri.queryoption.expression.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class FilterExpressionVisitor implements ExpressionVisitor<Object> {

    private final Entity currentEntity;


    public FilterExpressionVisitor(Entity currentEntity) {
        this.currentEntity = currentEntity;
    }

    @Override
    public Object visitTypeLiteral(EdmType type) throws ODataApplicationException {
        throw new ODataApplicationException("Type literals are not implemented",
                HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
    }

    @Override
    public Object visitMember(Member member) throws ODataApplicationException {
        final List<UriResource> uriResourceParts = member.getResourcePath().getUriResourceParts();

        if (uriResourceParts.size() == 1 && uriResourceParts.get(0) instanceof UriResourcePrimitiveProperty uriResourceProperty) {
            return currentEntity.getProperty(uriResourceProperty.getProperty().getName()).getValue();
        } else {
            throw new ODataApplicationException("Only primitive properties are implemented in filter expressions", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
        }
    }

    @Override
    public Object visitLiteral(Literal literal) throws ODataApplicationException {
        String literalAsString = literal.getText();
        if (literal.getType() instanceof EdmString) {
            String stringLiteral = "";
            if (literal.getText().length() > 2) {
                stringLiteral = literalAsString.substring(1, literalAsString.length() - 1);
            }

            return stringLiteral;
        }
        else if (literal.getType().getFullQualifiedName().toString().equals("Edm.Decimal")) {
            try {
                return BigDecimal.valueOf(Double.parseDouble(literalAsString));
            } catch (NumberFormatException e) {
                throw new ODataApplicationException("Invalid format for Edm.Double",
                        HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
            }
        }
        else {
            try {
                return Integer.parseInt(literalAsString);
            } catch (NumberFormatException e) {
                throw new ODataApplicationException("Only Edm.Int32 and Edm.String literals are implemented",
                        HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
            }
        }
    }

    public Object visitUnaryOperator(UnaryOperatorKind operator, Object operand)
            throws ODataApplicationException {

        if (operator == UnaryOperatorKind.NOT && operand instanceof Boolean) {
            // boolean negation
            return !(Boolean) operand;
        } else if (operator == UnaryOperatorKind.MINUS && operand instanceof Integer) {
            // arithmetic minus
            return -(Integer) operand;
        }

        throw new ODataApplicationException("Invalid type for unary operator",
                HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
    }

    @Override
    public Object visitMethodCall(MethodKind methodCall, List<Object> parameters) throws ODataApplicationException {
        if (methodCall == MethodKind.CONTAINS) {
            if (parameters.size() == 2 && parameters.get(0) instanceof String && parameters.get(1) instanceof String) {
                String field = (String) parameters.get(0);
                String value = (String) parameters.get(1);

                return field.contains(value);
            } else {
                throw new ODataApplicationException("Contains needs two parameters of type Edm.String",
                        HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
            }
        } else if (methodCall == MethodKind.STARTSWITH) {
            if (parameters.size() == 2 && parameters.get(0) instanceof String && parameters.get(1) instanceof String) {
                String field = (String) parameters.get(0);
                String value = (String) parameters.get(1);

                return field.startsWith(value);
            } else {
                throw new ODataApplicationException("StartsWith needs two parameters of type Edm.String",
                        HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
            }
        } else if (methodCall == MethodKind.ENDSWITH) {
            if (parameters.size() == 2 && parameters.get(0) instanceof String && parameters.get(1) instanceof String) {
                String field = (String) parameters.get(0);
                String value = (String) parameters.get(1);

                return field.endsWith(value);
            } else {
                throw new ODataApplicationException("EndsWith needs two parameters of type Edm.String",
                        HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
            }
        } else {
            throw new ODataApplicationException("Method call " + methodCall + " not implemented",
                    HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
        }
    }

    @Override
    public Object visitBinaryOperator(BinaryOperatorKind operator, Object left, Object right) throws ExpressionVisitException, ODataApplicationException {
        if (operator == BinaryOperatorKind.ADD
                || operator == BinaryOperatorKind.MOD
                || operator == BinaryOperatorKind.MUL
                || operator == BinaryOperatorKind.DIV
                || operator == BinaryOperatorKind.SUB) {
            return evaluateArithmeticOperation(operator, left, right);
        } else if (operator == BinaryOperatorKind.EQ
                || operator == BinaryOperatorKind.NE
                || operator == BinaryOperatorKind.GE
                || operator == BinaryOperatorKind.GT
                || operator == BinaryOperatorKind.LE
                || operator == BinaryOperatorKind.LT) {
            return evaluateComparisonOperation(operator, left, right);
        } else if (operator == BinaryOperatorKind.AND
                || operator == BinaryOperatorKind.OR) {
            return evaluateBooleanOperation(operator, left, right);
        } else {
            throw new ODataApplicationException("Binary operation " + operator.name() + " is not implemented", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
        }
    }

    @Override
    public Object visitLambdaExpression(String lambdaFunction, String lambdaVariable, Expression expression) throws ExpressionVisitException, ODataApplicationException {
        return null;
    }

    @Override
    public Object visitAlias(String aliasName) throws ExpressionVisitException, ODataApplicationException {
        return null;
    }

    @Override
    public Object visitLambdaReference(String variableName) throws ExpressionVisitException, ODataApplicationException {
        return null;
    }

    @Override
    public Object visitEnum(EdmEnumType type, List<String> enumValues) throws ExpressionVisitException, ODataApplicationException {
        return null;
    }

    @Override
    public Object visitBinaryOperator(BinaryOperatorKind operator, Object left, List<Object> right) throws ExpressionVisitException, ODataApplicationException {
        return null;
    }

    private Object evaluateBooleanOperation(BinaryOperatorKind operator, Object left, Object right)
            throws ODataApplicationException {

        // First check that both operands are of type Boolean
        if (left instanceof Boolean && right instanceof Boolean) {
            Boolean valueLeft = (Boolean) left;
            Boolean valueRight = (Boolean) right;

            // Than calculate the result value
            if (operator == BinaryOperatorKind.AND) {
                return valueLeft && valueRight;
            } else {
                // OR
                return valueLeft || valueRight;
            }
        } else {
            throw new ODataApplicationException("Boolean operations needs two numeric operands",
                    HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }
    }

    private Object evaluateComparisonOperation(BinaryOperatorKind operator, Object left, Object right) throws ODataApplicationException {

        if (left instanceof BigDecimal && right instanceof BigDecimal) {
            BigDecimal leftDecimal = (BigDecimal) left;
            BigDecimal rightDecimal = (BigDecimal) right;
            return compareBigDecimal(operator, leftDecimal, rightDecimal);
        }

        if (left instanceof LocalDate && right instanceof LocalDate) {
            return compareLocalDate(operator, (LocalDate) left, (LocalDate) right);
        }
        if (left.getClass().equals(right.getClass())) {
            int result;
            if (left instanceof Integer) {
                result = ((Comparable<Integer>) (Integer) left).compareTo((Integer) right);
            } else if (left instanceof String) {
                result = ((Comparable<String>) (String) left).compareTo((String) right);
            } else if (left instanceof Boolean) {
                result = ((Comparable<Boolean>) (Boolean) left).compareTo((Boolean) right);
            } else {
                throw new ODataApplicationException("Class " + left.getClass().getCanonicalName() + " not expected",
                        HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);
            }

            if (operator == BinaryOperatorKind.EQ) {
                return result == 0;
            } else if (operator == BinaryOperatorKind.NE) {
                return result != 0;
            } else if (operator == BinaryOperatorKind.GE) {
                return result >= 0;
            } else if (operator == BinaryOperatorKind.GT) {
                return result > 0;
            } else if (operator == BinaryOperatorKind.LE) {
                return result <= 0;
            } else {
                // BinaryOperatorKind.LT
                return result < 0;
            }

        } else {
            throw new ODataApplicationException("Comparison needs two equal types",
                    HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }
    }

    private Object evaluateArithmeticOperation(BinaryOperatorKind operator, Object left,
                                               Object right) throws ODataApplicationException {

        // First check if the type of both operands is numerical
        if (left instanceof Integer && right instanceof Integer) {
            Integer valueLeft = (Integer) left;
            Integer valueRight = (Integer) right;

            // Than calculate the result value
            if (operator == BinaryOperatorKind.ADD) {
                return valueLeft + valueRight;
            } else if (operator == BinaryOperatorKind.SUB) {
                return valueLeft - valueRight;
            } else if (operator == BinaryOperatorKind.MUL) {
                return valueLeft * valueRight;
            } else if (operator == BinaryOperatorKind.DIV) {
                return valueLeft / valueRight;
            } else {
                // BinaryOperatorKind,MOD
                return valueLeft % valueRight;
            }
        } else {
            throw new ODataApplicationException("Arithmetic operations needs two numeric operands", HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }
    }

    private Boolean compareBigDecimal(BinaryOperatorKind operator, BigDecimal left, BigDecimal right) {
        int result = left.compareTo(right);
        return evaluateComparisonResult(operator, result);
    }

    private Boolean compareLocalDate(BinaryOperatorKind operator, LocalDate left, LocalDate right) {
        int result = left.compareTo(right);
        return evaluateComparisonResult(operator, result);
    }

    private Boolean evaluateComparisonResult(BinaryOperatorKind operator, int result) {
        switch (operator) {
            case EQ:
                return result == 0;
            case NE:
                return result != 0;
            case GE:
                return result >= 0;
            case GT:
                return result > 0;
            case LE:
                return result <= 0;
            case LT:
                return result < 0;
            default:
                return false;
        }
    }

    @Override
    public Object visitComputeAggregate(AggregateExpression aggregateExpr) throws ExpressionVisitException, ODataApplicationException {
        return ExpressionVisitor.super.visitComputeAggregate(aggregateExpr);
    }
}
