$(document).ready(function(){
		
	$(".sw-android").click( function() {
		
		var imageNew = $(this).parent().parent().find(".and-image");
		var imageOld = $(this).parent().parent().find(".iph-image");
		var id = $(this).parent().parent().find("img").attr("id");
		var iphone = $(this).parent().find(".sw-iphone");
		var android = $(this);
		
		imageNew.html('<img class="sw-img-android" id="'+ id +'" src="public/20140618/image/information-android-' + id + '.png" title="Android" alt="Android">');
		imageOld.hide();
		imageNew.fadeIn("slow");
        iphone.removeClass("sw-selected");
        android.addClass("sw-selected");
		
	});
	
	
	$(".sw-iphone").click( function() {
		
		var imageOld = $(this).parent().parent().find(".and-image");
		var imageNew = $(this).parent().parent().find(".iph-image");
		var id = $(this).parent().parent().find("img").attr("id");
		var iphone = $(this);
		var android = $(this).parent().find(".sw-android");
		
		imageNew.html('<img class="sw-img-iphone" id="'+ id +'" src="public/20140618/image/information-iphone-' + id + '.png" title="iPhone" alt="iPhone">');
		imageOld.hide();
		imageNew.fadeIn("slow");
        iphone.addClass("sw-selected");
        android.removeClass("sw-selected");
		
	});
    
    $(".sw-img-iphone").live( 'click' , function() {

		var imageOld = $(this).parent();
        var id = $(this).attr("id");
        var iphone = $(this).parent().parent().find("a.sw-iphone");
		var android = $(this).parent().parent().find("a.sw-android");
        
        imageOld.html('<img class="sw-img-android" id="'+ id +'" src="public/20140618/image/information-android-' + id + '.png" title="Android" alt="Android">');
        iphone.removeClass("sw-selected");
        android.addClass("sw-selected");
    });
    
     $(".sw-img-android").live( 'click' , function() {
 
		var imageOld = $(this).parent();
        var id = $(this).attr("id");
        var iphone = $(this).parent().parent().find("a.sw-iphone");
		var android = $(this).parent().parent().find("a.sw-android");
         
        imageOld.html('<img class="sw-img-iphone" id="'+ id +'" src="public/20140618/image/information-iphone-' + id + '.png" title="iPhone" alt="iPhone">');
        iphone.addClass("sw-selected");
        android.removeClass("sw-selected");
    });
    
//    $(".virtual-card-a").click( function() {
//        $(".credit-box-info").hide();
//        $(".virtual-box-info").fadeIn( "slow" );
//        $(".debit-card-box").removeClass("switched-tarif");
//        $(".credit-card-box").addClass("switched-tarif");
//    });
//    
//    $(".debit-card-a").click( function() {
//        $(".virtual-box-info").hide();
//        $(".credit-box-info").fadeIn( "slow" );
//        $(".credit-card-box").removeClass("switched-tarif");
//        $(".debit-card-box").addClass("switched-tarif");
//    });
			
});
