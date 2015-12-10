$(document).on('change', '.btn-file :file', function() {
  var input = $(this),
      numFiles = input.get(0).files ? input.get(0).files.length : 1,
      label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
  input.trigger('fileselect', [numFiles, label]);
});

$(document).ready(function () {

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
    displayImages("webresources/model.image")

    //Show sign up form
    $('#signUp').click(function () {
        $('#myMod1').modal('show');

    });
    //Show sign in form
    $('#signIn').click(function () {
        $('#myMod').modal('show');
    });
    
    //Show upload form
    $('#upload').click(function()   {
        $('#myMod2').modal('show'); 
    });
    
    //Show file name
    $('.btn-file :file').on('fileselect', function(event, numFiles, label) {

        var input = $(this).parents('.input-group').find(':text'),
            log = numFiles > 1 ? numFiles + ' files selected' : label;

        if( input.length ) {
            input.val(log);
        } else {
            if( log ) alert(log);
        }

    });
    
    //Display images
    function displayImages(link) {
        $.get(link, function (xml) {
            var data = $.xml2json(xml);
            //console.log(data.image[1].path);
            //$('#image-feed').empty();
            console.log($.isArray(data.image));
            $.each(data.image, function (index, value) {
                console.log(value.path);
                $('#image-feed').append('<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">' +
                        '<div class="photo-box">' +
                        '<div class="image-wrap">' +
                        '<img class="img-responsive" src="http://192.168.56.1/test/' + value.path + '">' +
                        '<!--                                <div class="likes">309 Likes</div>-->' +
                        '</div>' +
                        '<div class="description">' +
                        value.description +
                        ' <div class="date">' + value.itime + '</div>' +
                        '</div>' +
                        '  </div>' +
                        ' </div>');
                //            console.log("succeeded");
            });
        });
    }
    //Search function
    $('#searchButton').click(function (e) {
        e.preventDefault();
        $.get("webresources/model.image/search/" + $('#searchInput').val(), function (xml) {
            var data = $.xml2json(xml);
            //console.log(data.image[1].path);
            if (!$.isArray(data.image)) {
                data.image = [data.image];
            }
            $('#image-feed').empty();
            $.each(data.image, function (index, value) {
                console.log(value.path);
                $('#image-feed').append('<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">' +
                        '<div class="photo-box">' +
                        '<div class="image-wrap">' +
                        '<img class="img-responsive" src="http://192.168.56.1/test/' + value.path + '">' +
                        '<!--                                <div class="likes">309 Likes</div>-->' +
                        '</div>' +
                        '<div class="description">' +
                        value.description +
                        ' <div class="date">' + value.itime + '</div>' +
                        '</div>' +
                        '  </div>' +
                        ' </div>');
                //            console.log("succeeded");
            });
        });
    });

    $('#workpls2').click(function (e) {
        //e.preventDefault();
        var data = {
            username: "Bob",
            password: "hihi"
        };
        $.ajax({
            type: "POST",
            url: "http://192.168.56.1:8080/ImageSharing/webresources/model.user/testjson",
            contentType: 'application/json',
            data: JSON.stringify(data),
//            data: {"username":"hihi", "password":"huhu"},
            success: function (response) {
                console.log(response);

            }
        });
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
        //console.log(check);
    });

    $("#logOut").click(function () {
        localStorage.removeItem("checkUserLogin");
        localStorage.removeItem("UID");
        location.reload();
    });

    $('#workpls').click(function () {
        console.log(localStorage.getItem("checkUserLogin"));
        console.log(localStorage.getItem("UID"));
    });

    //    function workcmnd() {
    //        $.ajax({
    //            type: "GET",
    //            url: "http://192.168.56.1:8080/ImageSharing/webresources/model.user/logIn/test100/test1001",
    //            dataType: "text",
    //            success: function (response) {
    //                console.log(response);
    //                if (response == "YES") {
    //                    //                    check = true;
    //                    //                    console.log(check);
    //                    localStorage.setItem("check", true);
    //                }
    //            }
    //        });
    //    }
});