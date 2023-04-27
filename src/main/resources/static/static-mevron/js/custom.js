$(function () {
    // =====================================================
    //    NAVBAR SCROLL
    // =====================================================
    var c, currentScrollTop = 0;
    $(window).on('scroll load', function () {

        if ($(window).scrollTop() >= 70) {
            $('.navbar').addClass('fixed-top');
        } else {
            $('.navbar').removeClass('fixed-top');
        }

    });
	
	// =====================================================
    //    WOW
    // =====================================================
	var $window = $(window);

    if ($window.width() > 767) {
        new WOW().init();
    }

});


// =====================================================
//    Sign Slide Down & Slide Up & Login
// =====================================================
	jQuery(document).ready(function() {		
		jQuery(".signup-btn").click(function(){
		  jQuery(".modal_login").hide();	
		  jQuery(".modal_signup").slideDown();	
		  jQuery("body").addClass("overflow-hidden");
		  jQuery("header .navbar").addClass("fixed-menu");
		});
		
		jQuery(".close_btn").click(function(){
		  jQuery(".modal_signup").slideUp();	
		  jQuery("body").removeClass("overflow-hidden");
		  jQuery("header .navbar").removeClass("fixed-menu");
		});
		
		jQuery(".login-btn").click(function(){
     	   jQuery(".modal_signup").slideUp();
		  jQuery(".modal_login").slideDown();	
		  jQuery("body").addClass("overflow-hidden");
		  jQuery("header .navbar").addClass("fixed-menu");
		});
		
		jQuery(".close-login").click(function(){
		  jQuery(".modal_login").hide();	
		  jQuery("body").removeClass("overflow-hidden");
		  jQuery("header .navbar").removeClass("fixed-menu");
		});
		
	});
	

// =====================================================
//    NAV TAB ACTIVE BORDER MOVE
// =====================================================
jQuery(function(){
	var menu = jQuery(".menu_tab");
		var indicator = jQuery('<span class="indicator"></span>');
		menu.append(indicator);
		position_indicator(menu.find("li.active"));  
		setTimeout(function(){indicator.css("opacity", 1);}, 500);
		menu.find("li").click(function(){
			position_indicator(jQuery(this));			
		});
			  
	function position_indicator(ele){
		var left1 = jQuery(".menu_tab").offset();	
		var activeLiOffset=ele.offset().left;
		//alert(activeLiOffset);
		var subtractleft=activeLiOffset-left1.left;
		//alert("Top coordinate: " + left1.top +  "   Left Coordinate: " + left1.left); 		
		//alert(left1+' pos123');
		var width = ele.width();
		//alert(width);
		  indicator.stop().animate({
			left: subtractleft,
			width: width
		});
	}
});

// =====================================================
//    Responsive SCROLLING MENU
// =====================================================
jQuery(".menu_tab li").on("click", function() {
  jQuery(".menu_tab li").removeClass("active");
  jQuery(this).addClass("active");
  // CALL scrollCenter PLUSGIN
  jQuery(".menu_tab").scrollCenter(".active", 300);
});

jQuery.fn.scrollCenter = function(elem, speed) {

  var active = jQuery(this).find(elem); // find the active element  
  var activeWidth = active.width() / 2; // get active width center

  //alert(activeWidth)

  var pos = active.position().left + activeWidth; //get left position of active li + center position
  var elpos = jQuery(this).scrollLeft(); // get current scroll position
  var elW = jQuery(this).width(); //get div width
  var divwidth = jQuery(elem).width(); //get div width
  pos = pos + elpos - elW / 3; // for center position if you want adjust then change this

  jQuery(this).animate({
    scrollLeft: pos
  }, speed == undefined ? 1000 : speed);
  return this;
};

jQuery.fn.scrollCenterORI = function(elem, speed) {
  jQuery(this).animate({
    scrollLeft: jQuery(this).scrollLeft() - jQuery(this).offset().left + jQuery(elem).offset().left
  }, speed == undefined ? 1000 : speed);
  return this;
};

// =====================================================
//    Invite Code Copy
// =====================================================

function copyCode() {
  /* Get the text field */
  var copyText = document.getElementById("codeCopy");

  /* Select the text field */
  copyText.select(); 
  copyText.setSelectionRange(0, 99999); /*For mobile devices*/

  /* Copy the text inside the text field */
  document.execCommand("copy");

  /* Alert the copied text */
  alert("Copied the text: " + copyText.value);
}

// Book Cab
jQuery(".location_list li").on("click", function() {
  jQuery(".car_list").show();
  jQuery(".location_list").hide();  
});

$(".car_list").on('click', function(){
     window.location = "pick-drop.html";    
});

// Modal hide inside Modal
$(document).ready(function(){
    // Hide modal on button click
    $(".hide-modal").click(function(){
        $("#confirmation_payment, #add_tip").modal('hide');
    });
});

// Trip Completed
setTimeout(function() {
    $('#trip-completed').modal();
}, 8000);

// careew-detail blog slider
$('.slider-nav').slick({
   slidesToShow: 3,
   slidesToScroll: 1,
   asNavFor: '.slider-for',
   dots: false,
   focusOnSelect: true,
     responsive: [
    {
      breakpoint: 991,
      settings: {
        slidesToShow: 2,
        slidesToScroll: 1,
        infinite: true,
        dots: false
      }
    },
    {
      breakpoint: 600,
      settings: {
        slidesToShow: 1,
        slidesToScroll: 1
      }
    },
    
  ]
 });