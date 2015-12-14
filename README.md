# Image Sharing

## Group 8
 - Phan Dang Hai
 - Stuart Bowman
 - Mahady Hassan Chowdhury 

## Functionality
 - Log in (checks if user gives correct username and password)
 - Sign up (creates new user, doesn't allow user to use username that has been already taken)
 - Upload (uploads images to server, it automatically generates filename for the image based on the ID of the image so that if the user uploads an image with the same filename, it doesn't overwrite the existing one. User can add tags and description to the image in the upload form)
 - Search (search images by tags, usernames in the search bar. If user clicks on username or tags, it will return all images of that user/tags)
 - Browse images (when user clicks on an image, it shows the larger image, likes, owner, description, tags, link and comments of that image)
 - Like (user can like images if they are logged in, otherwise it will show up the sign-in form, user can like an image only once)
 - Comment (user can comment on any images if they are logged in, otherwise it will show up the sign-in form)
 
 ##Example codes
 
 Search by username and tags
 ```Java
 @GET
    @Path("search/{input}")
    @Produces({"application/xml", "application/json"})
    public List<Image> searchImage(@PathParam("input") String input) {
        ArrayList<Image> resultList = new ArrayList<Image>();
        if (em.createNamedQuery("User.findByUname").setParameter("uname", input).getResultList().size() != 0) {
            User user = (User) em.createNamedQuery("User.findByUname").setParameter("uname", input).getSingleResult();
            resultList.addAll(user.getImageCollection1());
        }
        if (em.createNamedQuery("Tag.findByTagname").setParameter("tagname", input).getResultList().size() != 0) {
            Tag tag = (Tag) em.createNamedQuery("Tag.findByTagname").setParameter("tagname", input).getSingleResult();
            resultList.addAll(tag.getImageCollection());
        }
        return resultList;
    }
```    
    Get number of likes of an image
 ```Java
 @GET
    @Path("getLike/{id}")
    @Produces("text/plain")
    public String getLike(@PathParam("id") Integer id) {
        return String.valueOf(super.find(id).getUserCollection().size());
    }
 ```