package unisa.is.guardatv.controller.servlet;

import unisa.is.guardatv.StorageLayer.ListaDAO;
import unisa.is.guardatv.controller.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static unisa.is.guardatv.controller.Constants.*;


/**
 * Servlet implementation class EliminazioneLista
 */
@WebServlet(name = "EliminazioneLista", urlPatterns = "/eliminazione-lista")
public class EliminazioneListaServlet extends HttpServlet {

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminazioneListaServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String nome = request.getParameter("nome");
        // controllo il nome in input che rispetti la lunghezza definita
        if (!Utils.getInstance().checkStringLength(nome, MIN_NAME_LENGTH, MAX_NAME_LENGTH)) {
            response.getWriter().write(BAD_REQUEST_MESS);
            return;
        }

        String utente = request.getParameter("utente");
        // controllo che l'utente sia una stringa valida
        if (Utils.getInstance().isValidString(utente)) {
            response.getWriter().write(BAD_REQUEST_MESS);
            return;
        }

        ListaDAO listaDAO = new ListaDAO();

        try {
            listaDAO.doDelete(nome, utente);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write(BAD_REQUEST_MESS);
            return;
        }

        response.getWriter().write("ok");
    }
}



