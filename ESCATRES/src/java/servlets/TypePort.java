/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.orm.PersistentException;
import org.orm.PersistentTransaction;

/**
 *
 * @author Marcelo Esperguel
 */
@WebServlet(name = "TypePort", urlPatterns = {"/TypePort"})
public class TypePort extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PersistentException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
       
        switch (action) {
            case "list":
                list(request,response);
                break;
            case "add":
                add(request,response);
                break;
            case "delete":
                delete(request,response);
                break;
            default:
                break;
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (PersistentException ex) {
            Logger.getLogger(TypePort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (PersistentException ex) {
            Logger.getLogger(TypePort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    public void list(HttpServletRequest request, HttpServletResponse response) throws PersistentException, ServletException, IOException{
        String table = " "
                + " <table class=\"table\"> "
                + "                    <tr> "
                + "                            <td>Id(PK)</td>"
                + "                            <td>Name</td>"               
                + "                            <td></td>"
                + "                            <td></td>"
                + "                    </tr>";


        esca_orm.TypePort[] types = esca_orm.DAOFactory.getDAOFactory().getTypePortDAO().listTypePortByQuery(null, null);
        for(int i = 0; i < types.length; i++) {
                        table+="<tr>"
                                + "<td>"
                                + types[i].getORMID()
                                +"</td>"
                                + "<td>"
                                +types[i].getTypeName()
                                +"</td>"                                
                                + "<td>"
                                + "<a href=\"TypePort.jsp?action=search&id="+ types[i].getORMID()+"\">Edit</a>"
                                + "</td>"
                                + "<td>"
                                + "<a href=\"TypePort?action=delete&id="+ types[i].getORMID()+"\">Delete</a>"
                                + "</td>"
                     + "</tr>";
        }

        table+= "</table>";

        request.getSession().setAttribute("table", table);
        request.getRequestDispatcher("/backend/TypePort.jsp").forward(request, response);

    }
    
    public void add(HttpServletRequest request, HttpServletResponse response) throws PersistentException{
       
        String name= request.getParameter("name"); 
        
        
        PersistentTransaction t = esca_orm.EmbeddedSystemMMPersistentManager.instance().getSession().beginTransaction();

        try {
            esca_orm.DAOFactory lDAOFactory = esca_orm.DAOFactory.getDAOFactory();
            esca_orm.dao.TypePortDAO typeDao = lDAOFactory.getTypePortDAO();
            esca_orm.TypePort type = typeDao.createTypePort();
            // Initialize the properties of the persistent object here
            
            type.setTypeName(name);

            typeDao.save(type);
            
            t.commit();
            //response.sendRedirect("backend/GeneralPurpose.jsp");
            request.getRequestDispatcher("TypePort?action=list").forward(request, response); 
            //request.getRequestDispatcher("/backend/GeneralPurpose.jsp").forward(request, response);   
        }
        catch (Exception e) {
                t.rollback();
        }
        
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws PersistentException{
        int id= Integer.parseInt(request.getParameter("id"));
            
            PersistentTransaction t = esca_orm.EmbeddedSystemMMPersistentManager.instance().getSession().beginTransaction();
		try {
			esca_orm.DAOFactory lDAOFactory = esca_orm.DAOFactory.getDAOFactory();
                        esca_orm.dao.TypePortDAO typeDao = lDAOFactory.getTypePortDAO();
                        esca_orm.TypePort type = typeDao.createTypePort();
			// Delete the persistent object
			typeDao.delete(type);
			
			t.commit();
                        request.getRequestDispatcher("TypePort?action=list").forward(request, response); 
		}
		catch (Exception e) {
			t.rollback();
		}

    }
    
    public String dropDownList() throws ServletException, IOException, PersistentException{
        String dropDown = "<select id=\"typePortList\" name=\"typePortList\" class=\"form-control\">\n";
        
        esca_orm.TypePort[] types = esca_orm.DAOFactory.getDAOFactory().getTypePortDAO().listTypePortByQuery(null, null);
        for(int i = 0; i < types.length; i++) {
            
            dropDown+="<option value="+types[i].getORMID()+">"+types[i].getTypeName()+"</option>\n";
        }
                        
        dropDown+= "</select>";

        return dropDown;
    }
}
