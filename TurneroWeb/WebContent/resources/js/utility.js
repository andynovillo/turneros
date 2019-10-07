$(document).ready(function() {

    document.addEventListener('DOMContentLoaded', function() {
        if (!Notification) {
            alert('Las notificaciones no están permitidas en su navegador.');
            return;
        }

        if (Notification.permission !== "granted")
            Notification.requestPermission();

    });

});

function notifyMe(titleTxt, bodyTxt) {
    if (Notification.permission !== "granted")
        Notification.requestPermission();
    else {
        var notification = new Notification(titleTxt, {
            icon: pushIcon,
            body: bodyTxt,
            tag: 'multiple'
        });
        setTimeout(notification.close.bind(notification), 5000);
    }
}

function showGlobalMsg(data) {

    if (data) {

        var obj = null;

        try {

            obj = JSON.parse(data);

            if (obj.ctx === 'sticky') {
                $('.glb-msg').text(obj.msg);
            } else {
                PF('globalGrowl').renderMessage({
                    "summary": obj.msg,
                    "detail": "",
                    "severity": "info"
                });
            }

        } catch (err) {

        }

    }


}

var monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
];
var dayNames = ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"];

if (!String.prototype.format) {
    String.prototype.format = function() {
        var args = arguments;
        return (this + "").replace(/{(\d+)}/g, function(match, number) {
            return typeof args[number] != 'undefined' ? args[number] : match;
        });
    };
}

(function() {

    /**
	 * Decimal adjustment of a number.
	 * 
	 * @param {String}
	 *            type The type of adjustment.
	 * @param {Number}
	 *            value The number.
	 * @param {Integer}
	 *            exp The exponent (the 10 logarithm of the adjustment base).
	 * @returns {Number} The adjusted value.
	 */
    function decimalAdjust(type, value, exp) {
        // If the exp is undefined or zero...
        if (typeof exp === 'undefined' || +exp === 0) {
            return Math[type](value);
        }
        value = +value;
        exp = +exp;
        // If the value is not a number or the exp is not an integer...
        if (isNaN(value) || !(typeof exp === 'number' && exp % 1 === 0)) {
            return NaN;
        }
        // Shift
        value = value.toString().split('e');
        value = Math[type](+(value[0] + 'e' + (value[1] ? (+value[1] - exp) : -exp)));
        // Shift back
        value = value.toString().split('e');
        return +(value[0] + 'e' + (value[1] ? (+value[1] + exp) : exp));
    }

    // Decimal round
    if (!Math.round10) {
        Math.round10 = function(value, exp) {
            return decimalAdjust('round', value, exp);
        };
    }
    // Decimal floor
    if (!Math.floor10) {
        Math.floor10 = function(value, exp) {
            return decimalAdjust('floor', value, exp);
        };
    }
    // Decimal ceil
    if (!Math.ceil10) {
        Math.ceil10 = function(value, exp) {
            return decimalAdjust('ceil', value, exp);
        };
    }
})();

function startDropDownHover() {
    if ($(document).width() > 768) {
        $('ul.nav li.dropdown ').hover(
            function() {
                $(this).find('.dropdown-menu').stop(true, true).delay(10)
                    .fadeIn(150);
            },
            function() {
                $(this).find('.dropdown-menu').stop(true, true).delay(10)
                    .fadeOut(150);
            });
    }
}

// startDropDownHover();

$(document).on('click', PrimeFaces.escapeClientId('formLogin:changePassword'),
    function() {

        var button = $('#toggle-button');

        if (button.is(':visible'))
            button.click();

    });

/*
 * $(window).resize(function() { if
 * (PF('changePasswordDialog').getJQ().is(':visible')) {
 * PF('changePasswordDialog').initPosition(); } });
 */

function resizePanels() {
    $(
            '.ui-selectonemenu-panel, .ui-selectcheckboxmenu-panel, .ui-autocomplete-panel')
        .each(
            function() {

                var id = PrimeFaces.escapeClientId($(this).attr('id'))
                    .replace("_panel", "");

                $(this).width($(id).width());

            });
}

$(document).ready(resizePanels());

$(window).resize(function() {
    resizePanels();
});

$(window).resize(function() {
    if ($(document).width() <= 768) {
        $('ul.nav li.dropdown').off('mouseenter mouseleave');
    } else {
        // startDropDownHover();
    }
});

function enableLabelCheck() {

    $(function() {
        $('.chk-div').each(function() {

            var id = $(this).find('input').attr('id');
            $(this).find('label').attr('for', id);

        });
    });

}

enableLabelCheck();

// Create recursive menu

function startMenu() {

    generateMenu();

}

function generateMenu() {

    for (var i = 0; i < menuItems.length; i++) {

        var obj = new Object();
        obj.id = menuItems[i][0];
        obj.url = menuItems[i][1];
        obj.parent = menuItems[i][2];
        obj.isFile = menuItems[i][3];
        obj.name = menuItems[i][4];
        obj.order = menuItems[i][5];
        obj.isVisible = menuItems[i][6];
        obj.prettyURL = menuItems[i][7];

        if (obj.parent == 0 && !obj.isFile) {
            createMenuElement(obj);
        } else {
            createSubMenuElement(obj)
        }

    }

    activateMenu();

}

function createMenuElement(obj) {

    var liref = jQuery('<li/>', {
        id: 'li-ref-' + obj.id,
        class: "dropdown"
    });

    var aref = jQuery('<a/>', {
        id: 'a-ref-' + obj.id,
        href: '#',
        'data-toggle': 'dropdown',
        role: 'button',
        'aria-haspopup': true,
        'aria-expanded': false,
        class: "dropdown-toggle",
        text: obj.name
    });

    var spanref = jQuery('<span/>', {
        id: 'span-ref-' + obj.id,
        class: 'caret'
    });

    liref.append(aref.append(spanref));
    liref.appendTo('#ref-' + obj.parent);

}

function createSubMenuElement(obj) {

    var ulref;
    var liref;
    var aref;
    var spanref;

    var found = true;

    ulref = $('#li-ref-' + obj.parent).find('ul.dropdown-menu');

    if (ulref.length == 0) {

        ulref = jQuery('<ul/>', {
            id: 'ul-ref-' + obj.id,
            class: obj.isFile ? 'dropdown-menu' : 'dropdown-menu multi-level',
        });

        found = false;

    }

    liref = jQuery('<li/>', {
        id: 'li-ref-' + obj.id,
        class: obj.isFile ? "" : "dropdown-submenu"
    });

    aref = jQuery('<a/>', {
        id: 'a-ref-' + obj.id,
        href: obj.isFile ? contextPath +
            (obj.prettyURL != null ? obj.prettyURL : obj.url) : '#',
        'data-toggle': obj.isFile ? '' : 'dropdown',
        text: obj.name
    });

    ulref.append(liref.append(aref));

    if (!found)
        ulref.appendTo('#li-ref-' + obj.parent);

}

// End of function

var lastLinkClicked = null;
var keepLastLink = true;

function activateMenu() {

    $('.dropdown-submenu a')
        .click(
            function() {

                var actualLinkClicked = $(this);
                var actualID = actualLinkClicked.attr('id');
                var display = $(this).parent().children(
                    '.dropdown-menu').css('display');

                if (lastLinkClicked != null) {

                    var block = lastLinkClicked.parent().find('ul');
                    var changedFromTree = block.find('#' + actualID).length > 0 ? false :
                        true;

                    if (changedFromTree)
                        block.removeAttr("style");

                    keepLastLink = changedFromTree;

                }

                if (keepLastLink)
                    lastLinkClicked = actualLinkClicked;

                actualLinkClicked.parent().children('.dropdown-menu')
                    .css('display',
                        display == 'none' ? 'block' : 'none');

                if (actualLinkClicked.attr('href') == '#')
                    return false;

            });

}

if (PrimeFaces.widget.FileUpload === undefined) {

} else {

    PrimeFaces.widget.FileUpload.prototype.reset = function() {
        this.clearMessages();
        return this.init(this.cfg);
    }

}

if (PrimeFaces.widget.DataTable === undefined) {

} else {

    PrimeFaces.widget.DataTable.prototype.reset = function() {
        return this.init(this.cfg);
    }
}

if (PrimeFaces.widget.Schedule === undefined) {

} else {

    PrimeFaces.widget.Schedule.prototype.reset = function() {
        return this.init(this.cfg);
    }
}

if (PrimeFaces.widget.TextEditor === undefined) {

} else {

    PrimeFaces.widget.TextEditor.prototype.reset = function() {
        return this.init(this.cfg);
    }
}

function createPushMenu() {

    for (var i = 0; i < menuItems.length; i++) {

        var obj = new Object();
        obj.id = menuItems[i][0];
        obj.url = menuItems[i][1];
        obj.parent = menuItems[i][2];
        obj.isFile = menuItems[i][3];
        obj.name = menuItems[i][4];
        obj.order = menuItems[i][5];
        obj.isVisible = menuItems[i][6];
        obj.prettyURL = menuItems[i][7];

        createPushMenuElement(obj);

    }

    if (sessionCreated === 'true') {

        new mlPushMenu(document.getElementById('mp-menu'), document
            .getElementById('trigger'), {
                type: 'cover'
            });

    }

}

function createPushMenuElement(obj) {

    var liref = jQuery('<li/>', {
        id: 'li-mp-ref-' + obj.id
    });

    var aref = jQuery('<a/>', {
        id: 'a-mp-ref-' + obj.id,
        href: obj.isFile ? contextPath +
            (obj.prettyURL != null ? obj.prettyURL : obj.url) : '#',
        text: obj.name
    });

    var spanref = jQuery('<span/>', {
        id: 'span-mp-ref-' + obj.id,
        class: obj.isFile ? '' : 'fa fa-arrow-right'
    });

    var divref = jQuery('<div/>', {
        id: 'div-mp-ref-' + obj.id,
        class: 'mp-level'
    });

    var h2ref = jQuery('<h2/>', {
        id: 'h2-mp-ref-' + obj.id,
        text: obj.name
    });

    var backref = jQuery('<a/>', {
        id: 'back-mp-ref-' + obj.id,
        class: 'mp-back',
        text: 'Atrás',
        href: '#'
    });

    var ulref = jQuery('<ul/>', {
        id: 'ul-mp-ref-' + obj.id
    });

    var parentFound = false;
    var ulParent = null;

    ulParent = $('#ul-mp-ref-' + obj.parent);

    if (ulParent.length == 0)
        parentFound = true;

    divref.append(h2ref);
    divref.append(backref);
    divref.append(ulref);
    liref.append(aref.append(spanref));

    if (!obj.isFile)
        liref.append(divref);

    if (obj.parent == 0) {

        liref.appendTo('#mp-ref-' + obj.parent);

    } else {

        ulParent.append(liref);

    }

}

function createResponsiveMultiLevelMenu() {

    if (typeof menuItems === 'undefined') {
        return;
    }

    for (var i = 0; i < menuItems.length; i++) {

        var obj = new Object();
        obj.id = menuItems[i][0];
        obj.url = menuItems[i][1];
        obj.parent = menuItems[i][2];
        obj.isFile = menuItems[i][3];
        obj.name = menuItems[i][4];
        obj.order = menuItems[i][5];
        obj.isVisible = menuItems[i][6];
        obj.prettyURL = menuItems[i][7];

        createResponsiveMultiLevelMenuElement(obj);

    }

    if (sessionCreated === 'true') {

        $('#dl-menu').dlmenu({
            animationClasses: {
                classin: 'dl-animate-in-1',
                classout: 'dl-animate-out-1'
            }
        });

    }

}

function createResponsiveMultiLevelMenuElement(obj) {

    var liref = jQuery('<li/>', {
        id: 'li-rm-ref-' + obj.id
    });

    var aref = jQuery('<a/>', {
        id: 'a-rm-ref-' + obj.id,
        href: obj.isFile ? contextPath +
            (obj.prettyURL != null ? obj.prettyURL : obj.url) : '#',
        text: obj.name
    });

    var spanref = jQuery('<span/>', {
        id: 'span-rm-ref-' + obj.id,
        class: obj.isFile ? '' : 'fa fa-arrow-right'
    });

    var divref = jQuery('<div/>', {
        id: 'div-rm-ref-' + obj.id,
        class: 'rm-level'
    });

    var h2ref = jQuery('<h2/>', {
        id: 'h2-rm-ref-' + obj.id,
        text: obj.name
    });

    var backref = jQuery('<a/>', {
        id: 'back-rm-ref-' + obj.id,
        class: 'rm-back',
        text: 'Atrás',
        href: '#'
    });

    var ulref = jQuery('<ul/>', {
        id: 'ul-rm-ref-' + obj.id,
        class: 'dl-submenu'
    });

    var parentFound = false;
    var ulParent = null;

    ulParent = $('#ul-rm-ref-' + obj.parent);

    if (ulParent.length == 0)
        parentFound = true;

    // divref.append(h2ref);
    // divref.append(backref);
    // divref.append(ulref);
    liref.append(aref);

    if (!obj.isFile)
        liref.append(ulref);

    if (obj.parent == 0) {
        liref.appendTo('#rm-ref-' + obj.parent);

    } else {

        ulParent.append(liref);

    }

}

function createSideBarMenu() {

    if (typeof menuItems === 'undefined') {
        return;
    }

    for (var i = 0; i < menuItems.length; i++) {

        var obj = new Object();
        obj.id = menuItems[i][0];
        obj.url = menuItems[i][1];
        obj.parent = menuItems[i][2];
        obj.isFile = menuItems[i][3];
        obj.name = menuItems[i][4];
        obj.order = menuItems[i][5];
        obj.isVisible = menuItems[i][6];
        obj.prettyURL = menuItems[i][7];

        createSideBarMenuElement(obj);

    }

}

function createSideBarMenuElement(obj) {

    // Find the parent container

    var ulParent = null;

    ulParent = $('#ul-sb-ref-' + obj.parent);

    if (ulParent.length == 0) {

        ulParent = jQuery('<ul/>', {
            id: 'ul-sb-ref-' + obj.id,
            class: 'treeview-menu'
        });

    }

    // Node

    if (obj.isFile) {

        var liref = jQuery('<li/>', {
            id: 'li-sb-ref-' + obj.id
        });

        var aref = jQuery('<a/>', {
            id: 'a-sb-ref-' + obj.id,
            class: 'ellipsis',
            title: obj.name,
            href: obj.isFile ? contextPath +
                (obj.prettyURL != null ? obj.prettyURL : obj.url) : '#',
            text: obj.name
        });

        var spanref = jQuery('<span/>', {
            id: 'span-sb-ref-' + obj.id,
            class: ''
        });

        liref.append(aref.append(spanref));

        ulParent.append(liref);

    } else { // Parent

        var liparentref = jQuery('<li/>', {
            id: 'li-sb-parent-ref-' + obj.id,
            class: 'treeview'
        });
        var aparentref = jQuery('<a/>', {
            id: 'a-sb-parent-ref-' + obj.id,
            class: 'ellipsis',
            title: obj.name,
            href: '#'
        });
        var spanparentref = jQuery('<span/>', {
            text: obj.name
        });
        var spaniconparentref = jQuery('<span/>', {
            class: 'pull-right-container'
        });
        var iconparentref = jQuery('<i/>', {
            class: 'fa fa-angle-left pull-right'
        });

        spaniconparentref.append(iconparentref);
        aparentref.append(spanparentref);
        aparentref.append(spaniconparentref);
        liparentref.append(aparentref);

        var ulparentref = jQuery('<ul/>', {
            id: 'ul-sb-ref-' + obj.id,
            class: 'treeview-menu'
        });

        liparentref.append(ulparentref);

        ulParent.append(liparentref);

    }

}

var createCookie = function(name, value, days) {
    var expires;
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toGMTString();
    } else {
        expires = "";
    }
    document.cookie = name + "=" + value + expires + "; path=/";
}

function getCookie(c_name) {
    if (document.cookie.length > 0) {
        c_start = document.cookie.indexOf(c_name + "=");
        if (c_start != -1) {
            c_start = c_start + c_name.length + 1;
            c_end = document.cookie.indexOf(";", c_start);
            if (c_end == -1) {
                c_end = document.cookie.length;
            }
            return unescape(document.cookie.substring(c_start, c_end));
        }
    }
    return "";
}

$(document).ready(function() {

    if ($.browser.msie) {
        alert(wrongBrowser);
    }

    $('body').on('keyup', '.trigger-form', function(event) {

        var key = event.which || event.keyCode;
        if (key == 13) {
            $(this).find('.trigger-cmd').click();
        }

    });

    // if (localStorage.getItem(startup) === null) {
    //
    // localStorage.clear();
    // localStorage[startup] = true;
    // window.location.reload(true);
    //
    // }

    // if (getCookie('startup') === '' || getCookie('startup') !== startup) {
    // createCookie('startup', startup, null);
    // window.location.reload(true);
    // }

    // startMenu();
    // createPushMenu();

    // adminLTE
    if (typeof adminLTE !== 'undefined')
        createSideBarMenu();
    else
        createResponsiveMultiLevelMenu();

});

var enumerateDaysBetweenDates = function(startDate, endDate) {

    var dates = [];

    var currDate = moment(startDate).startOf('day');
    var lastDate = moment(endDate).startOf('day');

    while (currDate.diff(lastDate) <= 0) {
        dates.push(currDate.clone().toDate());
        currDate = currDate.add(1, 'days');
    }

    return dates;

};

var AdminLTEOptions = {
    // Enable sidebar expand on hover effect for sidebar mini
    // This option is forced to true if both the fixed layout and sidebar
    // mini
    // are used together
    sidebarExpandOnHover: true,
    // BoxRefresh Plugin
    enableBoxRefresh: true,
    // Bootstrap.js tooltip
    enableBSToppltip: false
};

/* Diálogos */

$(document).on(
    'show.bs.modal',
    '.modal',
    function() {
        var zIndex = 1040 + (10 * $('.modal:visible').length);
        $(this).css('z-index', zIndex);
        setTimeout(function() {
            $('.modal-backdrop').not('.modal-stack').css(
                'z-index', zIndex - 1).addClass(
                'modal-stack');
        }, 0);
    });


$(document).on(
    'hidden.bs.modal',
    '.modal',
    function() {
        $('.modal:visible').length &&
            $(document.body).addClass('modal-open');
    });



/*
 * 
 * Validaciones
 */

function evaluatePanel(pnl) {

    var pnlRules = [];
    var obj = {};

    $('.' + pnl).find('input:not([type=hidden]), input.required, select').each(function() {

        obj[$(this).data('name')] = {
            required: $(this).prop('required')
        }

        pnlRules.push(obj);

        obj = {};


    });

    $('#form').removeData('validator');

    var validator = $('#form')
        .validate({
            errorElement: "em",
            errorPlacement: function(error, element) {
                return true;
            },
            highlight: function(element, errorClass,
                validClass) {
                $(element).closest('.form-group').addClass(
                    "has-error").removeClass("has-success");
            },
            unhighlight: function(element, errorClass,
                validClass) {
                $(element).closest('.form-group').removeClass(
                    "has-error");
            },
            rules: pnlRules
        });

    var valid = true;

    $('.' + pnl).find('input:not([type=hidden]), input.required, select').each(function() {  	
    	
		if($(this).attr('id') !== undefined){
    	
	        validator.element($(PrimeFaces
	            .escapeClientId($(this).attr('id'))));
	        validator.focusInvalid();
	
	        if (validator.valid()) {
	            valid = true;
	        } else {
	            valid = false;
	            return valid;
	        }
        
		}

    });

    return valid;

}