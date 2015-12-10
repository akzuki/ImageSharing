package controller;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.transaction.UserTransaction;
import model.Image;
import model.Tag;
import model.User;
import model.service.ImageFacadeREST;

/**
 *
 * @author patricka
 */
@WebServlet(name = "Upload", urlPatterns = {"/upload"})
@MultipartConfig(location = "/var/www/html/test")
public class Upload extends HttpServlet {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ImageSharingPU");
    private EntityManager em;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
            transaction.begin();
            em = emf.createEntityManager();
            Image img = new Image();
            em.persist(img);
            transaction.commit();

            transaction.begin();
            em = emf.createEntityManager();
            String path = img.getIid().toString() + "_" + request.getPart("file").getSubmittedFileName();
            img.setPath(path);
            img.setDescription(request.getParameter("desInput"));
            int uid = Integer.parseInt(request.getParameter("userID"));
            User user = em.find(User.class, uid);
            img.setUid(user);

            Date date = new Date();
            img.setItime(date);
            //Set tag
            String tagInput = request.getParameter("tagInput");
            Tag tag = new Tag();
            if (em.createNamedQuery("Tag.findByTagname").setParameter("tagname", tagInput).getSingleResult() == null) {
                tag.setTagname(request.getParameter("tagInput"));
            } else {                
                tag = (Tag) em.createNamedQuery("Tag.findByTagname").setParameter("tagname", tagInput).getSingleResult();
            }
            ArrayList<Image> listImage = new ArrayList();
            listImage.add(img);
            tag.setImageCollection(listImage);

            ArrayList<Tag> listTag = new ArrayList();
            listTag.add(tag);
            img.setTagCollection(listTag);

            //img.getTagCollection()
            em.merge(img);
            //em.getTransaction().commit();
            transaction.commit();
            out.println(img.getPath());
            request.getPart("file").write(path);
            response.sendRedirect(request.getHeader("Referer"));
        } catch (Exception e) {
            out.println("Exception -->" + e.getMessage());
        } finally {
            out.close();
            em.close();
            //emf.close();
        }
    }

}
