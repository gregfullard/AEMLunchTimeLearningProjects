/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tech.innisfree.assignments.servlet.core.servlets;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Component(service=Servlet.class,
           property={
                   Constants.SERVICE_DESCRIPTION + "=Beverage Servlet",
                   "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                   "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                   "sling.servlet.resourceTypes="+ "ServletAssignment/servlet/beverage"
           })
public class BeverageServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUid = 1L;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        final Resource resource = req.getResource();
        resp.setContentType("text/plain");
        resp.getWriter().write("Title = " + resource.adaptTo(ValueMap.class).get("jcr:title"));
    }
    
    @Override
    protected void doPost(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        
      LOGGER.info("Beverage servlet invoked. Method = POST");
      
      String beverageName = null;
      String responseMsg = null;
      int responseCode = 0;

      try {

        // Extract the beverage name from the request
        RequestParameter param = req.getRequestParameter("beverageName");
        if ((param != null)&&(!param.toString().isEmpty())) {
          beverageName = param.toString();
        } else {
          responseMsg = "Incorrect input message. You must supply a valid HTTP form with a parameter called beverageName";
          LOGGER.error(responseMsg);
          responseCode = HttpServletResponse.SC_BAD_REQUEST;
          throw new Exception(responseMsg);
        }
        LOGGER.info("Beverage name: "+beverageName);
        
        // Get a session object to interactiwith the JCR
        Session session = req.getResourceResolver().adaptTo(Session.class);
        
        // Check if this is a duplicate
        boolean exists = session.nodeExists("/content/ServletAssignment/beverages/"+beverageName);
        
        if (exists) {
          responseMsg = "Uniqueness constraint violation. The beverage already exists in the JCR";
          LOGGER.error(responseMsg);
          responseCode = HttpServletResponse.SC_CONFLICT;
          throw new Exception(responseMsg);
        }

        // Add the new beverage
        Node beveragesRootNode = session.getNode("/content/ServletAssignment/beverages");
        beveragesRootNode.addNode(beverageName);
        session.save();
        
        responseCode = HttpServletResponse.SC_OK;
        responseMsg = "Beverage Added = " + beverageName; 
        
        // Check if the beverage is a Coffee
        if (beverageName.equalsIgnoreCase("Coffee")) {
          responseMsg = "Coffee is for the weak and timid - Prepare to be annihilated";
          LOGGER.warn(responseMsg);
          responseCode = 418;
        }

      } catch (Exception e) {
        
        // Handle unexpected exceptions
        if (responseCode == 0) {
          responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
          responseMsg = "An unexpected error has occurred";
          LOGGER.error(e.getMessage(), e);
        }
        
        if (responseMsg==null) {
          responseMsg = "An unexpected error has occurred";
        }
      }
      
      // Send the result back
      resp.setStatus(responseCode);
      resp.setContentType("text/plain");
      resp.getWriter().write(responseMsg);
    }
}
