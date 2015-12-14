$(document).on('change', '.btn-file :file', function () {
    var input = $(this),
            numFiles = input.get(0).files ? input.get(0).files.length : 1,
            label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    input.trigger('fileselect', [numFiles, label]);
});

$(document).ready(function () {

    //Check user login
    if (localStorage.getItem("checkUserLogin") == "true") {
        $('#signUp').hide();
        $('#signIn').hide();
        console.log("Log In : YES");
        $('#userID').val(localStorage.getItem("UID"));
    } else {
        $('#logOut').hide();
        $('#upload').hide();
        $('#myPhotos').hide();
        console.log("Log In: NO");
    }
    // Close the Responsive Menu on Menu Item Click
    $('.navbar-collapse ul li a').click(function () {
        $('.navbar-toggle:visible').click();
    });
    //Animate the nav-bar
    $(window).scroll(function () {
        if ($(".navbar").offset().top > 50) {
            $(".navbar-fixed-top").addClass("top-nav-collapse");
        } else {
            $(".navbar-fixed-top").removeClass("top-nav-collapse");
        }
    });
    //Display images
    displayImages("webresources/model.image");

    //Show sign up form
    $('#signUp').click(function () {
        $('#signUpForm').modal('show');
    });
    //Show sign in form
    $('#signIn').click(function () {
        $('#signInForm').modal('show');
    });

    //Show upload form
    $('#upload').click(function () {
        $('#uploadForm').modal('show');
    });

    //Show file name
    $('.btn-file :file').on('fileselect', function (event, numFiles, label) {
        var input = $(this).parents('.input-group').find(':text'),
                log = numFiles > 1 ? numFiles + ' files selected' : label;

        if (input.length) {
            input.val(log);
        } else {
            if (log)
                alert(log);
        }

    });


    //Search function
    $('#searchButton').click(function (e) {
        e.preventDefault();
        $('#searchResultText').html('<p>Search images by ' + "'" + $('#searchInput').val() + "'");
        displayImages("webresources/model.image/search/" + $('#searchInput').val());
    });

    //Sign up function
    $('#signUpButton').click(function (e) {
        e.preventDefault();
        var username = $('#inputUsernameSignup').val();
        var password = $('#inputPasswordSignup').val();
        var email = $('#inputEmailSignup').val();
        $.ajax({
            type: "POST",
            url: "http://192.168.56.1:8080/ImageSharing/webresources/model.user/signUp/" + username + "/" + password + "/" + email,
            success: function (response) {

                if (response == "NO") {
                    alert("Username is already taken...");
                } else {
                    alert("Done");
                    location.reload();
                }

            }
        });
    });

    //Log in function
    $('#logInButton').click(function (e) {
        e.preventDefault();
        var username = $('#inputUsernameLogin').val();
        var password = $('#inputPasswordLogin').val();
        $.ajax({
            type: "GET",
            url: "http://192.168.56.1:8080/ImageSharing/webresources/model.user/logIn/" + username + "/" + password,
            dataType: "text",
            success: function (response) {
                console.log(response);
                if (response != "NO") {
                    localStorage.setItem("checkUserLogin", true);
                    localStorage.setItem("UID", response);
                    location.reload();
                } else {
                    alert("Invalid username or password!!!");
                }
            }
        });
    });

    $('#logOut').click(function () {
        localStorage.removeItem("checkUserLogin");
        localStorage.removeItem("UID");
        location.reload();
    });

    //Show images of user
    $('#myPhotos').click(function (e) {
        e.preventDefault();
        searchByUserid(localStorage.getItem("UID"));
    });

    //Search by users commenting on the image
    $('#listComments').on('click', '.userComment', function (e) {
        var userid = $(this).attr('uid');
        $('#imagemodal').modal('hide');
        searchByUserid(userid);
    });

    //Search by tags of the image
    $('#tags').on('click', '.tagSearch', function (e) {
        var tagid = $(this).attr('tagid');
        $('#imagemodal').modal('hide');
        searchByTagid(tagid);
    });

    $('#gallery').on('click', '.photo-box', function (e) {
        e.stopPropagation();
        var IID = $(this).find('img').attr('iid');
        $('#imagepreview').attr('src', $(this).find('img').attr('src'));
        $('#imagepreview').attr('iid', IID);
        $.get("webresources/model.image/getOwner/" + IID, function (response) {
            $('#owner').html('<p>Uploaded by: ' + response + '</p>');
        });
        //Display the description
        $.get("webresources/model.image/getDesc/" + IID, function (response) {
            $('#description').html('<p>Description: ' + response + '</p>');
        });
        //Display the tags
        $.get("webresources/model.image/getTag/" + IID, function (response) {
            var data = $.xml2json(response);
            if (!$.isArray(data.tag)) {
                data.tag = [data.tag];
            }
            $('#tags').empty();
            var tags = "";
            $.each(data.tag, function (index, value) {
                tags += "#" + '<a class="tagSearch" tagid="' + value.tid + '"</a>' + value.tagname + " ";
            });
            $('#tags').html('<p>Tags: ' + tags + '</p>');
        });
        //Desplay the link of image
        $('#linkImage').html('<p>Link: ' + $(this).find('img').attr('src') + '</p>');

        //Display the comments
        $.get("http://192.168.56.1:8080/ImageSharing/webresources/model.image/getComment/" + IID, function (response) {
            var data = $.xml2json(response);
            if (!$.isArray(data.comment)) {
                data.comment = [data.comment];
            }
            $('#listComments').empty();
            $.each(data.comment, function (index, value) {
                $('#listComments').append('<li><a class="userComment" uid="' + value.uid.uid + '">[' + value.uid.uname + ']</a>: ' + value.cment + '</li>' + '<br>');
                //console.log(value.cment);
            });
        });
        //Display the likes
        $.get("http://192.168.56.1:8080/ImageSharing/webresources/model.image/getLike/" + IID, function (response) {
            $('#likes').html('<p>' + response + ' Likes</p>');
        });
        $('#imagemodal').modal('show');
    });

    //Comment function
    $('#submitComment').click(function (e) {
        e.stopPropagation();
        var commentInput = $('#commentInput').val();
        var IID = $('#imagepreview').attr('iid');
        //$('#imagepreview').attr('iid', IID); 
        $.ajax({
            type: "POST",
            url: "http://192.168.56.1:8080/ImageSharing/webresources/model.comment/submitComment/" + localStorage.getItem("UID") + "/" + IID + "/" + commentInput,
            success: function (response) {
                if (response == "OK") {
                    alert('Comment: Done');
                }
            },
            error: function (response) {
                //alert('Please log in...');
                $('#imagemodal').modal('hide');
                $('#signInForm').modal('show');
            }
        });
    });

    //Like function
    $('#submitLike').click(function (e) {
        e.stopPropagation();
        if (localStorage.getItem("checkUserLogin") == null) {
            $('#imagemodal').modal('hide');
            $('#signInForm').modal('show');
            return;
        } else {
            var IID = $('#imagepreview').attr('iid');
            $.ajax({
                type: "POST",
                url: "http://192.168.56.1:8080/ImageSharing/webresources/model.image/submitLike/" + localStorage.getItem("UID") + "/" + IID,
                success: function (response) {
                    alert('Like: Done');
                    //$('#imagemodal').hide().show();
                },
                error: function (response) {
                    alert('You already liked it...');
                }
            });
        }
    });


    //Display images
    function displayImages(link) {
        $.get(link, function (xml) {
            var data = $.xml2json(xml);
            //console.log(data.image[1].path);
            //$('#image-feed').empty();
            if (!$.isArray(data.image)) {
                data.image = [data.image];
            }
            $('#image-feed').empty();
            //console.log($.isArray(data.image));
            $.each(data.image, function (index, value) {
                //console.log(value.path);
                $('#image-feed').prepend('<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">' +
                        '<div class="photo-box">' +
                        '<div class="image-wrap">' +
                        '<img class="img-responsive" iid="' + value.iid + '" src="http://192.168.56.1/test/' + value.path + '">' +
                        '<!--                                <div class="likes">309 Likes</div>-->' +
                        '</div>' +
                        '<div class="description">' +
                        value.description +
                        ' <div class="date">' + value.itime + '</div>' +
                        '</div>' +
                        '  </div>' +
                        ' </div>');
            });
        });
    }

    //Search by user ID
    function searchByUserid(userid) {
        $.get("webresources/model.user/getUsername/" + userid, function (response) {
            $('#searchResultText').html('<p>Images uploaded by ' + "'" + response + "'");
            displayImages("webresources/model.image/searchByUserID/" + userid);
        });
    }

    //Search by tag ID
    function searchByTagid(tagid) {
        $.get("webresources/model.tag/getTagname/" + tagid, function (response) {
            $('#searchResultText').html('<p>Search images by ' + "'" + response + "'");
            displayImages("webresources/model.image/searchByTagID/" + tagid);
        });
    }
});