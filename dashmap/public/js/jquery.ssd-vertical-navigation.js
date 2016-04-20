;(function($) {

	inci=["All"];
	offe=["All"];
	time=["All"];
	resu=["All"];
	
	
	
	
    $.fn.ssdVerticalNavigation = function(options) {

        "use strict";

        var settings = $.extend({

            classMaster : 'master',
            classActive : 'active',
            classClickable : 'clickable'

        }, options);


        function _leftNavigationActiveMain(thisLi) {

            "use strict";

            thisLi
                .toggleClass(settings.classActive)
                .siblings()
                .removeClass(settings.classActive);

        }


        function _leftNavigationClick(thisParentUl, thisLi, event) {

            "use strict";

            if (thisParentUl.hasClass(settings.classMaster) && ! thisLi.hasClass(settings.classClickable)) {

                event.preventDefault();
                event.stopPropagation();

                _leftNavigationActiveMain(thisLi);

            }

        }


        return this.each(function() {
			

            "use strict";

            $(this)
                .addClass(settings.classMaster)
                .on('click',  'li a', function(event) {

                try {
	
                    var thisA = $(this),
                        thisLi = thisA.parent('li'),
                        thisParentUl = thisLi.parent('ul');
					
					if (thisA.attr("class")=="offense"){
						if (thisA.text()=="All"){
								
								thisLi.siblings()
									.removeClass(settings.classActive);
							}
						if (thisA.text()!="All"){
							if (in_array("All",offe)){
								offe.splice(offe.indexOf("All"),1);
								$("#offenseAll")
									.removeClass(settings.classActive);
							}
						}
						
						if (!in_array(thisA.text(),offe)){
							offe.push(thisA.text());
							
							thisLi.toggleClass(settings.classActive);
								
						} else {
							offe.splice(offe.indexOf(thisA.text()),1);
							if (thisA.text()=="All"){
								offe=[]
							}
							thisLi.removeClass(settings.classActive);
						}
					}
					
					if (thisA.attr("class")=="offenseCrime"){
						if (thisA.text()=="All"){
								
								thisLi.siblings()
									.removeClass(settings.classActive);
							}
						if (thisA.text()!="All"){
							if (in_array("All",inci)){
								inci.splice(inci.indexOf("All"),1);
								$("#offenseCrimeAll")
									.removeClass(settings.classActive);
							}
						}
						
						if (!in_array(thisA.text(),inci)){
							thisLi.toggleClass(settings.classActive);
							inci.push(thisA.text());
							
						} else {
							inci.splice(inci.indexOf(thisA.text()),1);
							if (thisA.text()=="All"){
								inci=[];
							}
							thisLi.removeClass(settings.classActive);
						}
					}
					
					if (thisA.attr("class")=="time"){
						if (thisA.text()=="All"){
								
								thisLi.siblings()
									.removeClass(settings.classActive);
							}
						if (thisA.text()!="All"){
							if (in_array("All",time)){
								time.splice(time.indexOf("All"),1);
								$("#timeAll")
									.removeClass(settings.classActive);
							}
						}
						
						if (!in_array(thisA.text(),time)){
							thisLi.toggleClass(settings.classActive);
							time.push(thisA.text());
						} else {
							
							time.splice(time.indexOf(thisA.text()),1);
							if (thisA.text()=="All"){
								time=[];
							}
							thisLi.removeClass(settings.classActive);
						}
					}
					
					if (thisA.attr("class")=="result"){
						if (thisA.text()=="All"){
								
								thisLi.siblings()
									.removeClass(settings.classActive);
							}
						if (thisA.text()!="All"){
							if (in_array("All",resu)){
								resu.splice(resu.indexOf("All"),1);
								$("#resultAll")
									.removeClass(settings.classActive);
							}
						}
						
						if (!in_array(thisA.text(),resu)){
							thisLi.toggleClass(settings.classActive);
							resu.push(thisA.text());
							
						} else {
							resu.splice(resu.indexOf(thisA.text()),1);
							console.log(thisA.text());
							if (thisA.text()=="All"){
								resu=[];
								
							}
							thisLi.removeClass(settings.classActive);
						}
					}
					if (in_array(thisA.attr("class"),["result","time","offenseCrime","offense"])){
						console.log("change");
						mapping(map,offe,inci,time,resu);
						
					}
					
                    _leftNavigationClick(thisParentUl, thisLi, event);

                } catch (errorMessage) {

                    console.log(errorMessage);

                }

            });

        });


    }

}(jQuery));