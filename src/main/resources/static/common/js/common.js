/** Utils **/
function isHTML(str) {
	var wrapper = document.createElement('div');
	wrapper.innerHTML = str;
	for (var c = wrapper.childNodes, i = c.length; i--; ) {
	  if (c[i].nodeType == 1) return true; 
	}
	return false;
}

function getValueOfHtmlStr(str) {
	var wrapper= document.createElement('div');
	wrapper.innerHTML= str;
	return wrapper.firstChild.value?wrapper.firstChild.value:"";
}

function getDOMfromStr() {
	var wrapper= document.createElement('div');
	wrapper.innerHTML= str;
	return wrapper.firstChild;
}
/** End Utils **/

/** Validate **/
function dispError(validElement, validMessage) {
	validElement.removeClass("valid-feedback");
	validElement.addClass("was-validated");
	validElement.html("");
	validElement.html(validMessage);
}

function hideError(validElement, validMessage) {
	validElement.removeClass("was-validated");
	validElement.addClass("valid-feedback");
}

function validateEmail(email) {
    const regex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return regex.test(String(email).toLowerCase());
}
/** End validate **/

/** Time **/
function setCalsClearButton (year,month,elem){  
	  
    var afterShow = function(){  
        var d = new $.Deferred();  
        var cnt = 0;  
        setTimeout(function(){  
            if(elem.dpDiv[0].style.display === "block"){  
                d.resolve();  
            }  
            if(cnt >= 500){  
                d.reject("datepicker show timeout");  
            }  
            cnt++;  
        },10);  
        return d.promise();  
    }();  
 
    afterShow.done(function(){  
 
        // datepickerのz-indexを指定  
        $('.ui-datepicker').css('z-index', 2000);  
 
        var buttonPane = $( elem ).datepicker( "widget" ).find( ".ui-datepicker-buttonpane" );  
 
        var btn = $('<button class="ui-datepicker-current ui-state-default ui-priority-primary ui-corner-all" type="button">Clear</button>');  
        btn.off("click").on("click", function () {  
                $.datepicker._clearDate( elem.input[0] );  
            });  
        btn.appendTo( buttonPane );  
    });  
}

const convertTime12to24 = (time12h) => {
  const [time, modifier] = time12h.split(' ');
  let [hours, minutes] = time.split(':');
  if (hours === '12') {
    hours = '00';
  }
  if (modifier === 'PM') {
    hours = parseInt(hours, 10) + 12;
  }
  return `${hours}:${minutes}`;
}

function timeConverter(UNIX_timestamp){
	return new Date(UNIX_timestamp).toLocaleTimeString("en-US")
}
function getCurrentDate() {
	var d = new Date();
	var month = d.getMonth()+1;
	var day = d.getDate();
	var output = d.getFullYear() + '-' +
	    ((''+month).length<2 ? '0' : '') + month + '-' +
	    ((''+day).length<2 ? '0' : '') + day;
	return output;
  }
/** End Time **/