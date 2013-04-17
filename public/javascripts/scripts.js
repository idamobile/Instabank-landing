(function($){

    /**
     * Document Ready Intializations
    */
	$(document).ready(function(){
	
		// inin newsletter form ajax
		initNewsletterAjax();
        initSubscribe();

		$('#invite').click(function(){
			slideTo($('.footer'));
		});
	});


    /**
     * Window onLoad Initializations
    */
	$(window).load(function(){

		/*
		$(window).scroll(function(){
			stickyNewsletter();
		});
		*/
	});


	function slideTo(elem)
	{
		$('html, body').animate({
			scrollTop: elem.offset().top
		}, 1300, 'easeInOutCubic', function(){});
	}


    /**
     * Initialize Newsletter Ajax Signup Functionality
    */
	function initNewsletterAjax()
	{
		// set vars
		var form = $('#subscribe')
			actionUrl = form.attr('action');

        // set confirmation code
        $("input[name='code']").val(extras);

		// add submit event to form
		form.submit(function(e){
			
			e.preventDefault();
			var email = $('.input-text', form).val();
			
			// if input is empty, display message
			if('' == email){
				showMessage('invalid', 'Пожалуйста, введите адрес электронной почты');
				return;
			}
			
			// show Progress
			showMessage('progress', subscribe_progress);
			
			// submit the form using Ajax
			$.ajax({
	  			url: actionUrl,
	  			data: form.serialize(),
	  			dataType: 'json',
	  			success: function(d){
	  				if('null' == d){
						showMessage('error', 'Что-то пошло не так.');
		  				
	  				}else{
		  				if(d.result == "true"){
		  					showMessage('success', d.msg);
                            _gaq.push(['_trackPageview', '/subscribe']);
		  				}else{
		  					showMessage('invalid', d.msg);
		  				}
	  				}
	  			},
	  			error: function(d){
					showMessage('error', 'Что-то пошло не так.');
	  			}
			});
			
		});
	}
	
	
    function initSubscribe()
    {
        var vars = {};
        vars.form = $('#signup-form');
        vars.actionUrl = vars.form.attr('action');
        vars.responseCont = $('.result');
        vars.formContainer = $('.form-container');
        vars.button = vars.form.find('.button');
        vars.buttonInner = vars.button.find('span span');
        vars.overlay = $('.overlay');


        vars.form.submit(function(e){

            e.preventDefault();

            vars.overlay.show(0);
            vars.button.prop('disabled', true);
            vars.buttonInner.find('span').hide(0);
            vars.buttonInner.find('.loader').show(0);

            /**/
                // submit the form using Ajax
            $.ajax({
                url: vars.actionUrl,
                data: vars.form.serialize(),
                dataType: 'json',
                success: function(d){
                    if('null' == d){
                        showMessage('error', 'Подписка не удалась :(', vars);

                    }else{
                        vars.form_result = d.result;
                        if(d.result){
                            showMessage('success', d.msg, vars);
                            _gaq.push(['_trackPageview', '/subscribe']);
                        }else{
                            showMessage('invalid', d.msg, vars);
                        }
                    }
                },
                error: function(d){
                    showMessage('error', 'Подписка не удалась :(', vars);
                }
            });

        });

        vars.responseCont.find('button').click(function(){
			showMessage('hide', '', vars);
        });
    }


	/**
	 * Check if given string is an Email
	*/
	function isEmail(email) {
		var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		return regex.test(email);
	}

	
	/**
	 * Display Newsletter Input Message with Given Class and Text String
	*/
    function showMessage(className, msg, vars)
	{	
        var msgCont = vars.responseCont.find('.msg');
		
        if(className == 'invalid'){
            vars.responseCont.find('.button').show(0);
        }

		// if action is to hide message
		if(className == 'hide'){
            vars.overlay.hide();
            vars.button.removeProp('disabled');
            vars.buttonInner.find('.loader').hide(0);
            vars.buttonInner.find('span').show(0);
            vars.responseCont.slideUp(200).animate({opacity: 0}, 200, function(){ msgCont.html('') });
            vars.formContainer.slideDown(200).animate({opacity: 1}, 200);
				
		// otherwise display appropriate message
		}else{
            setTimeout(function(){
                vars.overlay.hide();
                vars.button.removeProp('disabled');
                msgCont.html(msg);
                vars.formContainer.slideUp(200).animate({opacity: 0}, 200);
                vars.responseCont.slideDown(200).animate({opacity: 1}, 200);
                if (vars.form_result == 'true') {
                    $('#form_name').val('');
                    $('#form_email').val('');
                }
            }, 2000);
		}
	}

    function stickyNewsletter()
    {
        if(isMobile()){
            return;
        }
        // declare vars
        var sbscrCont = $('.subscribe-container'),
            offsetTop = 570
        isFixed = sbscrCont.hasClass('fixed');
        windowTop = $(document).scrollTop();

        // fix the position
        if(windowTop > offsetTop && !isFixed){
            sbscrCont
                .hide(0)
                .addClass('fixed')
                .slideDown(250);

            // make element static
        }else if(windowTop < offsetTop && isFixed){
            sbscrCont
                .slideUp(100, function(){
                    sbscrCont
                        .removeClass('fixed')
                        .fadeIn(250);
                });
        }
    }


    function isMobile(){
        return (
            (navigator.userAgent.toLowerCase().indexOf("ipad") > -1) ||
                (navigator.userAgent.toLowerCase().indexOf("iphone") > -1) ||
                (navigator.userAgent.toLowerCase().indexOf("ipod") > -1)
            );
    }

}(jQuery))