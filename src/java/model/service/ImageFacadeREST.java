/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.service;

import java.util.ArrayList;
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
import model.Comment;
import model.Image;
import model.Tag;
import model.User;

/**
 *
 * @author HaiPhan
 */
@Stateless
@Path("model.image")
public class ImageFacadeREST extends AbstractFacade<Image> {

    @PersistenceContext(unitName = "ImageSharingPU")
    private EntityManager em;

    public ImageFacadeREST() {
        super(Image.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Image entity) {
        super.create(entity);
    }

    @GET
    @Path("search/{input}")
    @Produces({"application/xml", "application/json"})
    public List<Image> searchImage(@PathParam("input") String input) {
        User user = (User) em.createNamedQuery("User.findByUname").setParameter("uname", input).getResultList().get(0);
        return (List<Image>) user.getImageCollection1();
    }

    @GET
    @Path("searchByTag/{input}")
    @Produces({"application/xml", "application/json"})
    public List<Image> searchImageByTag(@PathParam("input") String input) {
//        ArrayList<Image> listImage = new ArrayList<Image>();
//        Tag tag = (Tag) em.createNamedQuery("Tag.findByTagname").setParameter("tagname", input).getSingleResult();
//        for (Image img : findAll()) {
//            if (img.getTagCollection().contains(tag)) {
//                listImage.add(img);
//            }
//        }
//        return listImage;
        Tag tag = (Tag) em.createNamedQuery("Tag.findByTagname").setParameter("tagname", input).getSingleResult();
        return (List<Image>) tag.getImageCollection();
    }

    @GET
    @Path("getDesc/{id}")
    @Produces("text/plain")
    public String getDesc(@PathParam("id") Integer id) {
        return super.find(id).getDescription();
    }

    @GET
    @Path("getTag/{id}")
    @Produces({"application/xml", "application/json"})
    public List<Tag> getTag(@PathParam("id") Integer id) {
        Image img = super.find(id);
        List<Tag> listTag = (List<Tag>) img.getTagCollection();
        return listTag;
    }

    @GET
    @Path("getComment/{id}")
    @Produces({"application/xml", "application/json"})
    public List<Comment> getComment(@PathParam("id") Integer id) {
        ArrayList<Comment> listComment = new ArrayList<Comment>();
        List<Comment> allComments = em.createNamedQuery("Comment.findAll").getResultList();
        for (Comment cm : allComments) {
            if (cm.getIid() == super.find(id)) {
                listComment.add(cm);
            }
        }
        return listComment;
    }

    @GET
    @Path("getLike/{id}")
    @Produces("text/plain")
    public String getLike(@PathParam("id") Integer id) {
        return String.valueOf(super.find(id).getUserCollection().size());
    }

    @GET
    @Path("getLikeTest/{id}")
    @Produces({"application/xml", "application/json"})
    public List<User> getLikeTest(@PathParam("id") Integer id) {
        return (List<User>) super.find(id).getUserCollection();
    }

    @POST
    @Path("submitLike/{uid}/{iid}")
    public void submitLike(@PathParam("uid") Integer uid, @PathParam("iid") Integer iid) {
        super.find(iid).getUserCollection().add(em.find(User.class, uid));
        em.find(User.class, uid).getImageCollection().add(super.find(iid));
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, Image entity) {
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
    public Image find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Image> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Image> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
