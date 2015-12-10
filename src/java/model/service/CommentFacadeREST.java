/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.service;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import model.Comment;
import model.Image;
import model.User;

/**
 *
 * @author HaiPhan
 */
@Stateless
@Path("model.comment")
public class CommentFacadeREST extends AbstractFacade<Comment> {
    @PersistenceContext(unitName = "ImageSharingPU")
    private EntityManager em;

    public CommentFacadeREST() {
        super(Comment.class);
    }
    
//    @POST
//    @Path("submitComment")
//    //@Consumes({"application/json"})
//    @Consumes({MediaType.APPLICATION_JSON})
//    public JsonObject submitComment(String msg)  {
//        JsonObject obj = new JsonParser().parse(msg).getAsJsonObject();
//        return obj;
//        
//        //return msg;
//    }
    @POST
    @Path("submitComment/{uid}/{iid}/{comment}")
    //@Consumes({"application/json"})
    public void submitComment(@PathParam("uid") Integer uid, @PathParam("iid") Integer iid, @PathParam("comment") String comment)  {
        Comment cm = new Comment();
        Date date = new Date();
        cm.setCtime(date);
        cm.setCment(comment);
        cm.setIid(em.find(Image.class, iid));
        cm.setUid(em.find(User.class, uid));
        super.create(cm);
    }
    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Comment entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, Comment entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Comment find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Comment> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Comment> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

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
