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
        List<UriResource> uriResourceParts = member.getResourcePath().getUriResourceParts();
        if (uriResourceParts.size() == 1 && uriResourceParts.get(0) instanceof UriResourcePrimitiveProperty) {
            UriResourcePrimitiveProperty uriResourceProperty = (UriResourcePrimitiveProperty) uriResourceParts.get(0);
            return currentEntity.getProperty(uriResourceProperty.getProperty().getName()).getValue();
        } else {
            throw new ODataApplicationException("Only primitive properties are implemented in filter expressions",
                    HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
        }
    }

    @Override
    public Object visitLiteral(Literal literal) throws ODataApplicationException {
        String literalAsString = literal.getText();
        EdmType type = literal.getType();

        if (type instanceof EdmString) {
            return literalAsString.substring(1, literalAsString.length() - 1);
        } else if ("Edm.Decimal".equals(type.getFullQualifiedName().toString())) {
            return new BigDecimal(literalAsString);
        } else {
            try {
                return Integer.parseInt(literalAsString);
            } catch (NumberFormatException e) {
                throw new ODataApplicationException("Invalid numeric format",
                        HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
            }
        }
    }

    @Override
    public Object visitUnaryOperator(UnaryOperatorKind operator, Object operand) throws ODataApplicationException {
        if (operator == UnaryOperatorKind.NOT && operand instanceof Boolean) {
            return !(Boolean) operand;
        } else if (operator == UnaryOperatorKind.MINUS && operand instanceof Integer) {
            return -(Integer) operand;
        } else {
            throw new ODataApplicationException("Invalid type for unary operator",
                    HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }
    }

    @Override
    public Object visitMethodCall(MethodKind methodCall, List<Object> parameters) throws ODataApplicationException {
        if (parameters.size() != 2 || !(parameters.get(0) instanceof String) || !(parameters.get(1) instanceof String)) {
            throw new ODataApplicationException("Method " + methodCall + " needs two parameters of type Edm.String",
                    HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }

        String field = (String) parameters.get(0);
        String value = (String) parameters.get(1);

        switch (methodCall) {
            case CONTAINS:
                return field.contains(value);
            case STARTSWITH:
                return field.startsWith(value);
            case ENDSWITH:
                return field.endsWith(value);
            default:
                throw new ODataApplicationException("Method call " + methodCall + " not implemented",
                        HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
        }
    }

    @Override
    public Object visitBinaryOperator(BinaryOperatorKind operator, Object left, Object right) throws ExpressionVisitException, ODataApplicationException {
        if (left instanceof Integer && right instanceof Integer) {
            return evaluateArithmeticOperation(operator, (Integer) left, (Integer) right);
        } else if (left instanceof Boolean && right instanceof Boolean) {
            return evaluateBooleanOperation(operator, (Boolean) left, (Boolean) right);
        } else {
            return evaluateComparisonOperation(operator, left, right);
        }
    }

    private Object evaluateArithmeticOperation(BinaryOperatorKind operator, Integer left, Integer right) throws ODataApplicationException {
        return switch (operator) {
            case ADD -> left + right;
            case SUB -> left - right;
            case MUL -> left * right;
            case DIV -> {
                if (right == 0) {
                    throw new ODataApplicationException("Division by zero is not allowed",
                            HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
                }
                yield left / right;
            }
            case MOD -> left % right;
            default -> throw new ODataApplicationException("Arithmetic operation not supported",
                    HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
        };
    }

    private Object evaluateBooleanOperation(BinaryOperatorKind operator, Boolean left, Boolean right) throws ODataApplicationException {
        return switch (operator) {
            case AND -> left && right;
            case OR -> left || right;
            default -> throw new ODataApplicationException("Boolean operation not supported",
                    HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
        };
    }

    @SuppressWarnings("unchecked")
    private Object evaluateComparisonOperation(BinaryOperatorKind operator, Object left, Object right) throws ODataApplicationException {
        if (!left.getClass().equals(right.getClass())) {
            throw new ODataApplicationException("Comparison operands must be of the same type",
                    HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }

        if (!(left instanceof Comparable<?> && right instanceof Comparable<?>)) {
            throw new ODataApplicationException("Both operands must be Comparable",
                    HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }

        Comparable<Object> comparableLeft = (Comparable<Object>) left;
        int comparisonResult = comparableLeft.compareTo(right);

        return switch (operator) {
            case EQ -> comparisonResult == 0;
            case NE -> comparisonResult != 0;
            case GT -> comparisonResult > 0;
            case GE -> comparisonResult >= 0;
            case LT -> comparisonResult < 0;
            case LE -> comparisonResult <= 0;
            default -> throw new ODataApplicationException("Comparison operation not supported",
                    HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
        };
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
}
