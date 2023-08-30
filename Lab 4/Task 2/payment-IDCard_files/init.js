//Jquery function used to transfer data between two (multi)selectboxes with help of images. Currently they
//need to be in a table
//jQuery.fn.multiPicker = function () {
//    return this.each(function () {
//        $imgs = $(this).find('img');
//        $img1 = $imgs.first();
//        $img2 = $imgs.last();

//        $img1.click(function () {
//            $container = $(this).closest('table');
//            $selects = $container.find('select');
//            $select1 = $selects.first();
//            $select2 = $selects.last();

//            $select1.find(':selected').remove().appendTo($select2);
//        });

//        $img2.click(function () {
//            $container = $(this).closest('table');
//            $selects = $container.find('select');
//            $select1 = $selects.first();
//            $select2 = $selects.last();

//            $select2.find(':selected').remove().appendTo($select1);
//        });
//    });
//};

$(window).load(function () {
    $(".DatePicker").datepicker({
        changeMonth: true,
        changeYear: true,
        closeText: "stäng",
        currentText: "idag",
        constrainInput: true,
        dayNames: ["söndag", "måndag", "tisdag", "onsdag", "torsdag", "fredag", "lördag"],
        dayNamesMin: ["sö", "må", "ti", "on", "to", "fr", "lö"],
        dayNamesShort: ["sön", "mån", "tis", "ons", "tor", "fre", "lör"],
        dateFormat: "yy-mm-dd",
        firstDay: 1,
        isRTL: false,
        monthNames: ["januari", "februari", "mars", "april", "maj", "juni", "juli", "augusti", "september", "oktober", "november", "december"],
        monthNamesShort: ["jan", "feb", "mar", "apr", "maj", "jun", "jul", "aug", "sep", "okt", "nov", "dec"],
        //maxDate: "+0d",
        nextText: "nästa",
        prevText: "föreg.",
        showMonthAfterYear: false,
        selectOtherMonths: true,
        showAnim: "",
        showOtherMonths: true,
        showWeek: true,
        weekHeader: "v.",
        yearSuffix: ""
    }).attr("autocomplete", "off"); // Turn off browser autocomplete for datepickers
});

$(document).ready(function () {
    $('[data-toggle=popover]').each(function () {
        var $element = $(this);
        var options = null;

        var source = $element.attr("data-source");
        if (source) {
            options = {
                content: $(source).html(),
                html: true
            };
        }

        $element.popover(options);
    });

    $("[data-function=color-subscriber]").each(function () {
        var $sample = $(this);
        var $source = $($sample.attr("data-provider"));

        $source.on("service.change", function (e, data) {
            $sample.css("background-color", data.service.color);
        });
    });

    // TODO: Borde gå att göra generell (och alltså inte specifik för Service)
    $("[data-function=data-subscriber], [data-function=data-provider-subscriber]").each(function () {
        var $element = $(this);
        var $source = $($element.attr("data-provider"));
        var key = $element.attr("data-key")
            || $element.attr("name")
            || $element.attr("id");

        if (!$element.is("datalist")) {
            if ($element.is("select")) {
                $source.on("data.load", loadSelectData);
            }
            else {
                $source.on("data.load", loadData);
            }
        }
        else {
            $source.on("data.load", $element.is("datalist") ? loadDataListData : loadData);

            var datalist = $element[0];
            $('#input_datalist_' + datalist.id).on('change textInput input', function (e) {
                var options = datalist.options;
                $(datalist).val('');
                $("#hidden_datalist_" + datalist.id).val('');
                for (var i = 0; i < options.length; i++) {
                    if (options[i].value === $(this).val()) {
                        var sectionId = $(options[i]).data("value");
                        $(datalist).val(sectionId);
                        $(datalist).trigger("change");
                        $("#hidden_datalist_" + datalist.id).val(sectionId);
                        break;
                    }
                }
            });
            $('#input_datalist_' + datalist.id).on('click', function (e) {
                var input = $('#input_datalist_' + datalist.id);
                input.val("");
            });
        }

        var shouldRefresh = $element.attr("post-after-data-provided");
        if (shouldRefresh != null && shouldRefresh === "True") {
            $source.on("data.load", function () {
                refreshAfterLoad();
            });
        }

        function loadSelectData(e, data) {
            var oldValue = $element.val();

            var items = data[key];
            $element.empty();

            if (!items || !(items instanceof Array))
                return;

            for (var i = 0; i < items.length; ++i) {
                var value = items[i].Value || items[i].value;
                var text = items[i].Text || items[i].text;

                var $option = $(document.createElement("option"))
                    .attr("value", value)
                    .text(text);

                if (value == oldValue)
                    $option.attr("selected", "selected");

                $option.appendTo($element);
            }

            $element.trigger("change");
        }

        function loadData(e, data) {
            var newValue = "";
            if (key == 'no-key') {
                newValue = data;
            }
            else {
                newValue = data[key];
            }

            var oldValue;

            if ($element.is(":checkbox"))
                oldValue = $element.is(":checked");
            else if ($element.is("span,div,label"))
                oldValue = $element.html();
            else
                oldValue = $element.val();

            if (oldValue == newValue)
                return;

            if ($element.is(":checkbox"))
                $element.prop("checked", newValue);
            else if ($element.is("span,div,label"))
                $element.html(newValue);
            else
                $element.val(newValue);

            $element.trigger("change");
        }

        function loadInputData(e, data) {
            var newValue = "";
            if (key == 'no-key') {
                newValue = data;
            }
            else {
                newValue = data[key];
            }

            var oldValue;

            if ($element.is(":checkbox"))
                oldValue = $element.is(":checked");
            else if ($element.is("span,div,label"))
                oldValue = $element.html();
            else
                oldValue = $element.val();

            if (oldValue == newValue)
                return;

            if ($element.is(":checkbox"))
                $element.prop("checked", newValue);
            else if ($element.is("span,div,label"))
                $element.html(newValue);
            else
                $element.val(newValue);

            $element.trigger("change");
        }

        function loadDataListData(e, data) {
            var oldValue = $element.val();

            var items = data[key];
            $element.empty();

            if (items.length > 0) {
                var firstText = items[0].Text || items[0].text;
                var firstValue = items[0].Value || items[0].value;

                $("#hidden_datalist_" + $element[0].id).val(firstValue);
                $("#input_datalist_" + $element[0].id).val(firstText);
            }
            else {
                $("#hidden_datalist_" + $element[0].id).val('');
                $("#input_datalist_" + $element[0].id).val('');
            }

            if (!items || !(items instanceof Array))
                return;

            for (var i = 0; i < items.length; ++i) {
                var text = items[i].Text || items[i].text;
                var value = items[i].Value || items[i].value;

                var $option = $(document.createElement("option"))
                    .attr("value", text)
                    .attr("data-value", value)

                if (value == oldValue) {
                    $option.attr("selected", "selected");
                    $("#hidden_datalist_" + $element[0].id).val(value);
                    $("#input_datalist_" + $element[0].id).val(text);
                }

                $option.appendTo($element);
            }

            $element.trigger("change");
        }
    });

    $("[data-function=data-provider], [data-function=data-provider-subscriber]").on("change load", function () {
        $.ajaxSetup({ cache: false });

        var $element = $(this);

        var url = $element.attr("data-url");
        var value = $element.val();

        if (!url || !value)
            return;

        var key = $element.attr("data-param")
            || $element.attr("name")
            || $element.attr("id")
            || "id";

        var parameters = [{
            name: key,
            value: value
        }];

        $.post(url, parameters, function (data) {
            $element.trigger("data.load", [data]);
        });
    });

    $("[data-function=data-broadcaster]").on("click", function () {
        $.ajaxSetup({ cache: false });

        var invoker = $(this);

        var targetId = invoker.attr('id').replace('Provider', 'Value');

        var $element = $('#' + targetId);

        var url = invoker.attr("data-url");
        var value = $element.val();

        if (!url || !value)
            return;

        var key = invoker.attr("data-param")
            || invoker.attr("name")
            || invoker.attr("id")
            || "id";

        var parameters = [{
            name: 'value',
            value: value
        },
        {
            name: 'name',
            value: key
        }
        ];

        $.post(url, parameters, function (data) {
            $(document).trigger("data.broadcast", [data]);
        });
    });

    $("[data-function=data-listener]").each(function () {
        var listener = $(this);

        var targetId = listener.attr('id').replace('Subscriber', 'Value');

        var $element = $('#' + targetId);

        var key = listener.attr("data-key")
            || listener.attr("name")
            || listener.attr("id");

        function loadBroadcastData(e, data) {
            var bookingFieldData = data.bookingFieldData;

            var targetData = bookingFieldData.filter(function (bfv) {
                return bfv.BookingFieldTextName === key;
            })[0];

            var newValue = targetData ? targetData.Value : "";

            var oldValue;

            if ($element.is(":checkbox"))
                oldValue = $element.is(":checked");
            else if ($element.is("span,div,label"))
                oldValue = $element.html();
            else
                oldValue = $element.val();

            if (oldValue == newValue)
                return;

            if ($element.is(":checkbox"))
                $element.prop("checked", newValue);
            else if ($element.is("span,div,label"))
                $element.html(newValue);
            else
                $element.val(newValue);

            $element.trigger("change");
        }

        $(document).bind('data.broadcast', loadBroadcastData);
    });

    $("[data-function=color-picker]").each(function () {
        $(this).jPicker({
            window:
            {
                position:
                {
                    x: 'screenCenter', // acceptable values "left", "center", "right", "screenCenter", or relative px value
                    y: 'center' // acceptable values "top", "bottom", "center", or relative px value
                }
            }
        });
    }).hide();

    $("form").submit(function () {
        $(".disable-on-save")
            .on("click", function (event) {
                event.preventDefault();
            })
            .addClass('disabled');
    });

    $("[data-function=submit-on-change]").on("change", function () {
        refreshAfterLoad();
    });

    var refreshAfterLoad = function () {
        var $element = $(this);

        var formId = $element.attr("form-id");

        if (!formId) {
            document.forms[0].submit();
        }

        $("#" + formId).submit();
    };

    //$("body").ajaxModal("[data-toggle=ajax-modal]");

    //$("[data-function=detect-changes]").livequery(function () {
    //    $(this).each(function () {
    //        var $submit = $(this)
    //            .find("input[type=submit]")
    //            .attr("disabled", "disabled")
    //            .addClass("disabled");

    //        $(this).detectChanges("input:not([name$=MetaData]):not(:hidden[name=Index]), textarea, select", function (hasChanges) {
    //            if (!hasChanges)
    //                $submit.attr("disabled", "disabled").addClass("disabled");
    //            else
    //                $submit.removeAttr("disabled").removeClass("disabled");
    //        }, { attribute: "name" });
    //    });
    //});

    /*** MultiPicker ***/
    //$('.MultiPicker').multiPicker();

    ////Select all items in selectboxes (will be in tables with class MultiPicker)
    //$("form").submit(function () {
    //    $('.MultiPicker select option').each(function () {
    //        $(this).attr("selected", "selected");
    //    });
    //});

    ////Unselects selectboxes at page load
    //$(".MultiPicker select option:selected").each(function () {
    //    $(this).removeAttr("selected");
    //});
});

// Checks/unchecks all checkboxes under element.
function checkAll(element, check) {
    checkboxes = element.find(":checkbox").prop("checked", check);
}

//Shows a confirmation dialog box with specified message. Returns true if user click ok,
//false otherwise.
function confirmSubmit(message) {
    var agree = confirm(message);
    if (agree)
        return true;
    else
        return false;
}

function isNumeric(value) {
    var intRegex = /^\d+$/;

    if (intRegex.test(value)) {
        return true;
    }

    return false;
}

function isDecimal(value) {
    var floatRegex = /^((\d+(\.\d *)?)|((\d*\.)?\d+))$/;

    if (floatRegex.test(value)) {
        return true;
    }

    return false;
}