package com.store.servlet;


import com.store.provider.EdmProvider;
import com.store.service.processor.EntityCollectionProcessor;
import com.store.service.processor.EntityProcessor;
import com.store.service.processor.PrimitiveProcessor;
import com.store.service.storage.EntityStorage;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;

import java.util.ArrayList;

@WebServlet(name = "ODataServlet", urlPatterns = "/odata/*")
public class OdataServlet extends HttpServlet {


    private final EntityStorage storage;

    public OdataServlet(EntityStorage storage) {
        this.storage = storage;
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            EntityStorage storage = getOrInitializeStorage(req);
            ODataHttpHandler handler = createODataHandler(storage);

            // Process OData request
            handler.process(req, resp);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error processing OData request", e);
        }
    }

    // Initialize EntityStorage in session.
    private EntityStorage getOrInitializeStorage(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        EntityStorage sessionStorage = (EntityStorage) session.getAttribute(EntityStorage.class.getName());
        if (sessionStorage == null) {
            sessionStorage = this.storage;
            session.setAttribute(EntityStorage.class.getName(), sessionStorage);
        }
        return sessionStorage;
    }

    // Create and configure the OData handler.
    private ODataHttpHandler createODataHandler(EntityStorage storage) {
        OData odata = OData.newInstance();
        ServiceMetadata edm = odata.createServiceMetadata(new EdmProvider(), new ArrayList<>());
        ODataHttpHandler handler = odata.createHandler(edm);

        // Set the entity processors
        handler.register(new EntityCollectionProcessor(storage));
        handler.register(new EntityProcessor(storage));
        handler.register(new PrimitiveProcessor(storage));

        return handler;
    }
}
