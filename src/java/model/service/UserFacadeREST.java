/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.service;

import java.io.StringReader;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jdk.nashorn.internal.parser.JSONParser;
import model.Image;
import model.User;

/**
 *
 * @author HaiPhan
 */
@Stateless
@Path("model.user")
public class UserFacadeREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "ImageSharingPU")
    private EntityManager em;

    public UserFacadeREST() {
        super(User.class);
    }

    @POST
    @Path("testjson")
    //@Consumes({"application/json"})
    @Consumes({MediaType.APPLICATION_JSON})
    public String create(String msg)  {
        User user = new User();
        user.setUname("pls");
        user.setUpass("pls");
        super.create(user);
        return msg;
    }
    
    @POST
    @Path("signUp/{username}/{password}/{email}")
    //@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String create(@PathParam("username") String username, @PathParam("password") String password, @PathParam("email") String email) {
        List<User> listUser = super.findAll();
        boolean check = true;
        for (User u : listUser) {
            if (u.getUname().equals(username)) {
                check = false;
            }
        }
        if (check) {
            User entity = new User();
            entity.setUname(username);
            entity.setUpass(password);
            entity.setUemail(email);
            super.create(entity);
            return "YES";
        } else {
            return "NO";
        }
//        em.find(Image.class, 48).setUid(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, User entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("logIn/{username}/{password}")
    //@Produces({"application/xml", "application/json"})
    public String logIn(@PathParam("username") String username, @PathParam("password") String password) {
        List<User> listUser = super.findAll();
        String uid = "";
        for (User u:listUser)   {
            if (u.getUname().equals(username) && u.getUpass().equals(password))     {
                uid = u.getUid().toString();
                return uid;
            }
        }
        return "NO";
    }
    
//    @GET
//    @Path("{id}")
//    @Produces({"application/xml", "application/json"})
//    public User find(@PathParam("id") Integer id) {
//        return super.find(id);
//    }
//
//    @GET
//    @Override
//    @Produces({"application/xml", "application/json"})
//    public List<User> findAll() {
//        return super.findAll();
//    }
//
//    @GET
//    @Path("{from}/{to}")
//    @Produces({"application/xml", "application/json"})
//    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
//        return super.findRange(new int[]{from, to});
//    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
