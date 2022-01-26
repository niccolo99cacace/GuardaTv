package unisa.is.guardatv.controller.servlet.GestioneUtente;

import unisa.is.guardatv.StorageLayer.Utente;
import unisa.is.guardatv.StorageLayer.UtenteDAO;
import unisa.is.guardatv.StorageLayer.Login;
import unisa.is.guardatv.StorageLayer.LoginDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 422938517959400504L;
    private final UtenteDAO utenteDAO = new UtenteDAO();
    private final LoginDAO loginDAO = new LoginDAO();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Utente utente = null;
        Utente utenteDaLoggare = null;
        if (email != null && password != null) {
            utente = utenteDAO.doRetrieveByEmail(email);
            String salt = utente.getSalt();
            String passwordSalt = password + salt;
            if (passwordSalt.equalsIgnoreCase(utente.getPasswordhash()))
                utenteDaLoggare = utenteDAO.doRetrieveByEmailPassword(email, passwordSalt);
        }

        if (utenteDaLoggare == null) {
            throw new unisa.is.guardatv.controller.servlet.MyServletException("email e/o password non validi.");
        }

        Login login = new Login();
        login.setIdUtente(utenteDaLoggare.getEmail());
        login.setToken(UUID.randomUUID().toString());
        login.setTime(Timestamp.from(Instant.now()));

        loginDAO.doSave(login);

        Cookie cookie = new Cookie("login", login.getId() + "_" + login.getToken());
        cookie.setMaxAge(30 * 24 * 60 * 60); // 30 giorni
        response.addCookie(cookie);

        request.getSession().setAttribute("utente", utenteDaLoggare);

        String dest = request.getHeader("referer");
        if ( (dest == null || dest.contains("/Login") || dest.contains("/Registrazione") || dest.trim().isEmpty()) ) {
            dest = ".";
        }
        response.sendRedirect(dest);
    }
}
