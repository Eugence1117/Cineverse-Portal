<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
    <meta charset="ISO-8859-1">
    <title><fmt:message key="theatre.type.title" /></title>

    <%@ include file="include/css.jsp"%>
    <link rel="stylesheet" href="<spring:url value='/plugins/datatables/dataTables.bootstrap4.min.css'/>">
    <link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
    <style>
        .fontBtn:hover{
            cursor:pointer;
        }
        .actionColumn{
            text-align:center
        }
    </style>
</head>

<body id="page-top">
<div id="wrapper">
    <%@ include file="include/sidebar.jsp" %>
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <%@ include file="include/topbar.jsp" %>
            <div class="container-fluid">
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Theatre Type</h1>
                </div>

                <div class="card m-2">
                    <div class="card-header">
                        <span class="fa fa-store-alt"></span> <span>Theatre Types</span>
                        <div class="fa-pull-right d-inline-block">
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table id="theatretypeInfo" class="table table-bordered table-hover" style="width:100% !important">
                                <thead>
                                <tr>
                                    <th>Theatre Type</th>
                                    <th>Description</th>
                                    <th>Theatre Capacity (Seat Available)</th>
                                    <th>Ticket Price (RM)</th>
                                    <th>Action</th>
                                </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <!--  END CONTENT -->
        </div>
        <footer class="sticky-footer bg-white">
            <div class="container my-auto">
                <div class="copyright text-center my-auto">
                    <span><fmt:message key="common.copyright" /></span>
                </div>
            </div>
        </footer>
    </div>
</div>

<div class="modal fade" id="editType" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Theatre Type Details</h5>
                <button type="button" class="close" data-bs-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body placeholder-glow">
                <form id="editForm">
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control data" id="seqid" data-json-key="seqid" name="typeId" placeholder="Theatre Type" disabled="disabled">
                        <label for="seqid">Theatre Type</label>
                    </div>
                    <div class="form-floating mb-3">
                        <textarea class="form-control data" id="desc" data-json-key="desc" name="description" placeholder="Description" style="height: 100px"></textarea>
                        <label for="desc">Description</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control data" id="seatSize" data-json-key="seatSize" name="seatSize" placeholder="Seat Capacity" disabled="disabled">
                        <label for="seatSize">Theatre Capacity</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control data" id="price" data-json-key="price" placeholder="Price (RM)" name="price">
                        <label for="price">Ticket Price (RM)</label>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" onclick="editTheatreType()">Apply</button>
            </div>
        </div>
    </div>
</div>
<%@ include file="/jsp/include/globalElement.jsp" %>
<!-- /.container -->

<%@ include file="include/js.jsp"%>
<script type="text/javascript" src="<spring:url value='/plugins/jquery-validation/jquery.validate.min.js'/>"></script>
<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
<script type="text/javascript" src="<spring:url value='/plugins/datatables/jquery.dataTables.min.js'/>"></script>
<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.bootstrap4.js'/>"></script>
<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.buttons.js'/>"></script>
<script type="text/javascript" src="<spring:url value='/plugins/datatables/jszip.min.js'/>"></script>
<script type="text/javascript" src="<spring:url value='/plugins/datatables/buttons.html5.min.js'/>"></script>
<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
<script type="text/javascript">
    var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");

    $.validator.setDefaults({
        errorElement : "div",
        errorClass : "invalid-feedback",
        highlight : function(element, errorClass, validClass) {
            // Only validation controls
            if (!$(element).hasClass('novalidation')) {
                $(element).closest('.form-control').removeClass(
                    'is-valid').addClass('is-invalid');
            }
        },
        unhighlight : function(element, errorClass, validClass) {
            // Only validation controls
            if (!$(element).hasClass('novalidation')) {
                $(element).closest('.form-control')
                    .removeClass('is-invalid').addClass('is-valid');
            }
        },
        errorPlacement : function(error, element) {
            error.insertAfter(element);
        }
    });

    $(document).ready(function(){
        readyFunction();

    });
    <!--FOR DISPLAY DATA TABLE-->
    function readyFunction(){
        $.ajax("api/admin/getAllTheatreType.json",{
            method : "GET",
            accepts : "application/json",
            dataType : "json",
            statusCode:{
                400:function(){
                    window.location.href = "400.htm";
                },
                401:function(){
                    window.location.href = "expire.htm";
                },
                403:function(){
                    window.location.href = "403.htm";
                },
                404:function(){
                    window.location.href = "404.htm";
                }
            },
        }).done(function(data){
            var resultDt = getResultDataTable().clear();
            if(data.errorMsg == null){
                addActionButton(data.result);
                resultDt.rows.add(data.result).draw();
                addTooltip();
            }
            else{
                bootbox.alert(data.errorMsg);
            }
        })
    }

    function addTooltip() {
        new jBox('Tooltip', {
            attach: '.editBtn',
            content: 'Edit'
        });
    }

    $("#editForm").validate({
        ignore : ".ignore",
        rules : {
            description:{
                required:true,
            },
            price:{
                required:true,
                number:true
            },
        }
    });

    function removeValidator(){
        $("#editForm .data").removeClass("is-valid").removeClass("is-invalid");
    }

    function getResultDataTable() {

        return $('#theatretypeInfo').DataTable({
            columns: [
                { data: 'seqid', 'width':'15%'},
                { data: 'desc','width':'35%'},
                { data: 'seatSize','width':'25%'},
                { data: 'price','width':'15%',render: $.fn.dataTable.render.number( ',', '.', 2, '' ) },
                { data: 'action','width':'10%'}
            ],
            dom:"<'row'<'col-md-6'l><'col-md-6'f>>" +
                "<'row'<'col-md-12't>><'row'<'col-md-12'i>><'row py-2'<'col-md-6'B><'col-md-6'p>>",
            buttons: [
                {
                    text:'Copy to clipboard',
                    extend: 'copy',
                    className: 'btn btn-primary',
                    exportOptions: {
                        columns: [ 0, 1, 2, 3, 4]
                    },
                },
                {
                    text:'Export as CSV(.csv)',
                    extend: 'csv',
                    className: 'btn btn-secondary',
                    exportOptions: {
                        columns: [ 0, 1, 2, 3, 4]
                    }
                },
                {
                    text:'Export as Excel(.xlsx)',
                    extend: 'excel',
                    className: 'btn btn-secondary',
                    exportOptions: {
                        columns: [ 0, 1, 2, 3, 4]
                    }
                },
            ],
            order: [],
            lengthMenu: [ [10, 25, 50, -1], [10, 25, 50, "All"] ],
            retrieve: true,
            fixedHeader: true,
            responsive:true,
            rowReorder: {
                selector: 'td:nth-child(2)'
            },
        });
    }

    function addActionButton(data){
        var editBtn = '<svg class="bi bi-pencil-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg"> '
            + '<path d="M15.502 1.94a.5.5 0 010 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 01.707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 00-.121.196l-.805 2.414a.25.25 0 00.316.316l2.414-.805a.5.5 0 00.196-.12l6.813-6.814z"/> '
            + '<path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 002.5 15h11a1.5 1.5 0 001.5-1.5v-6a.5.5 0 00-1 0v6a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11a.5.5 0 01.5-.5H9a.5.5 0 000-1H2.5A1.5 1.5 0 001 2.5v11z" clip-rule="evenodd"/> '
            + '</svg>';

        $.each(data, function(index, value) {
            value.action = "<p class='my-auto actionColumn'>";
            value.action += "<span class='p-1 mx-1 fontBtn editBtn' id='" + value.seqid +"' onclick=getTheatreTypeDetails(this)>" + editBtn + "</span>";
            value.action +="</p>"
        });
    }

    function clearModalDetails(){
        $("#editType .data").each(function(){
            $(this).val("");
        })
    }

    function getTheatreTypeDetails(element){
        var typeId = element.id;

        clearModalDetails();
        if(!$("#editType").hasClass("show")){
            $("#editType").modal("show");
        }

        $.ajax("api/authorize/getTheatreType.json?typeId=" + typeId,{
            method : "GET",
            accepts : "application/json",
            dataType : "json",
            statusCode:{
                400:function(){
                    window.location.href = "400.htm";
                },
                401:function(){
                    window.location.href = "expire.htm";
                },
                403:function(){
                    window.location.href = "403.htm";
                },
                404:function(){
                    window.location.href = "404.htm";
                }
            },
        }).done(function(data){
            if(data.errorMsg == null){
                $("#editType .modal-body .data").each(function(index,element){
                    var key = $(this).data('json-key');
                    if (key && data.result.hasOwnProperty(key)) {
                        $(this).val(data.result[key]|| "-");
                    }
                });
            }
            else{
                bootbox.alert(data.errorMsg,function(){
                    $("#editType").modal("hide");
                });
            }
        })
    }

    function editTheatreType(){
        var validator = $("#editForm").validate();
        if(!validator.form()){
            return false;
        }

        $("#editType").modal("hide");
        bootbox.confirm("Are you sure to update the theatre type?",function(result){
            if(result){
                var formData = $("#editForm").serializeObject();
                formData["typeId"] = $("#editForm input[name=typeId]").val()


                $.ajax("api/admin/updateTheatreType.json",{
                    method : "POST",
                    accepts : "application/json",
                    dataType : "json",
                    data:JSON.stringify(formData),
                    contentType:"application/json; charset=utf-8",
                    headers:{
                        "X-CSRF-Token": CSRF_TOKEN
                    },
                    statusCode:{
                        400:function(){
                            window.location.href = "400.htm";
                        },
                        401:function(){
                            window.location.href = "expire.htm";
                        },
                        403:function(){
                            window.location.href = "403.htm";
                        },
                        404:function(){
                            window.location.href = "404.htm";
                        }
                    },
                }).done(function(data){
                    if(data.errorMsg == null){
                        var toast = createToast(data.result,"An attempt to edit theatre type <b>Success</b>",true);
                        removeValidator()
                        clearModalDetails();
                        readyFunction();
                    }
                    else{
                        var toast = createToast(data.errorMsg,"An attempt to edit theatre type <b>Failed</b>",false);
                    }
                });
            }
            else{
                $("#editType").modal("show");
            }
        })
    }
</script>
</body>

</html>
