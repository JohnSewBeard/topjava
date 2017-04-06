package ru.javawebinar.topjava.web;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import ru.javawebinar.topjava.AuthorizedUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger LOG = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("forward to users");

        String id = request.getParameter("userId");
        AuthorizedUser.setId(Strings.isNullOrEmpty(id) ? 1 : Integer.parseInt(id));

        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}
