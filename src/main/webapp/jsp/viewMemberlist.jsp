<%@ include file="include/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<head>
<meta charset="ISO-8859-1">
<title><fmt:message key="member.view.title" /></title>

<%@ include file="include/css.jsp"%>
<link rel="stylesheet" href="<spring:url value='/plugins/JBox/JBox.all.min.css'/>">
<link rel="stylesheet" href="<spring:url value='/plugins/datatables/dataTables.bootstrap4.min.css'/>">
<style>
	.fontBtn{
		cursor:pointer;
	}
	.data{
		margin-bottom:0.5rem;
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
			        	<h1 class="h3 mb-0 text-gray-800">Members</h1>
			        </div>
		        	
		        	<div class="card m-2">
						<div class="card-header">
							<span class="fa fa-users"></span> <span>Members</span>							
						</div>
						<div class="card-body">
							<div class="table-responsive">
								<table id="memberInfo" class="table table-bordered table-hover" style="width:100% !important">
									<thead>
										<tr>
											<th>Member ID</th>
											<th>Name</th>											
											<th>Birth Date</th>
											<th>Email</th>
											<th>Status</th>
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
	
	<%@ include file="/jsp/include/globalElement.jsp" %>
	
	<!-- View Modal -->
	<div class="modal fade" id="viewMember" tabindex="-1" role="dialog">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Member Details</h5>
					<button type="button" class="close" data-bs-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="row placeholder-glow my-2">
						<label class="col-md-4"><b>Member ID</b></label> <label
							class="col-md-1 colon">:</label>
						<p class="d-inline data col-md-6" data-json-key="seqid"></p>
					</div>
					<div class="row placeholder-glow my-2">
						<label class="col-md-4"><b>Name</b></label> <label
							class="col-md-1 colon">:</label>
						<p class="d-inline data col-md-6" data-json-key="name"></p>
					</div>
					<div class="row placeholder-glow my-2">
						<label class="col-md-4"><b>Username</b></label> <label
							class="col-md-1 colon">:</label>
						<p class="d-inline data col-md-6" data-json-key="username"></p>
					</div>
					<div class="row placeholder-glow my-2">
						<label class="col-md-4"><b>Birth Date</b></label> <label
							class="col-md-1 colon">:</label>
						<p class="d-inline data col-md-6" data-json-key="dateOfBirth"></p>
					</div>
					<div class="row placeholder-glow my-2">
						<label class="col-md-4"><b>Email</b></label> <label
							class="col-md-1 colon">:</label>
						<p class="d-inline data col-md-6" data-json-key="email"></p>
					</div>
					<div class="row placeholder-glow my-2">
						<label class="col-md-4"><b>Status</b></label> <label
							class="col-md-1 colon">:</label>
						<p class="d-inline data col-md-6" data-json-key="status"></p>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary mx-auto"
						data-bs-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>

	<!-- /.container -->

<%@ include file="include/js.jsp"%>
	<script type="text/javascript" src="<spring:url value='/plugins/bootbox/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/JBox/JBox.all.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/jquery.dataTables.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.buttons.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/dataTables.bootstrap4.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/jszip.min.js'/>"></script>
	<script type="text/javascript" src="<spring:url value='/plugins/datatables/buttons.html5.min.js'/>"></script>
	
	<script type="text/javascript">
		var CSRF_TOKEN = $("meta[name='_csrf']").attr("content");
    	var CSRF_HEADER = $("meta[name='_csrf_header']").attr("content");
    	
    	$(document).ready(function(){
    		readyFunction()
    	})
    	
    	function readyFunction(){
			$.ajax("api/admin/getMembers.json",{
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
    	
    	function addTooltip(){
			new jBox('Tooltip', {
				attach : '.activeBtn',
				content : 'Activate'
			});
			new jBox('Tooltip',{
				attach :'.deactiveBtn',
				content : 'Deactivate'
			})
			new jBox('Tooltip', {
				attach : '.viewBtn',
				content : 'View'
			});
		}
    	
		function getResultDataTable() {
	   		
			return $('#memberInfo').DataTable({
				//autowidth:false,
				columns: [
					{ data: 'seqid', 'width':'25%'},
					{ data: 'name','width':'20%'},		   			
		   			{ data: 'dateOfBirth','width':'15%'},
		   			{ data: 'email','width':'20%'},
		   			{ data: 'status','width':'10%'},
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
  		             		columns: [ 0, 1, 2, 3, 4, 5]
				     	}
				    },
				    {
					   	text:'Export as CSV(.csv)',
					   	extend: 'csv',
						className: 'btn btn-secondary',
					    exportOptions: {
					       columns: [ 0, 1, 2, 3, 4, 5]
					    }
					},	
				    {
						text:'Export as Excel(.xlsx)',
					   	extend: 'excel',
						className: 'btn btn-secondary',
					    exportOptions: {
					        columns: [ 0, 1, 2, 3, 4, 5]
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
			var viewBtn = '<svg class="bi bi-eye" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">'
		          + '<path fill-rule="evenodd" d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.134 13.134 0 001.66 2.043C4.12 11.332 5.88 12.5 8 12.5c2.12 0 3.879-1.168 5.168-2.457A13.134 13.134 0 0014.828 8a13.133 13.133 0 00-1.66-2.043C11.879 4.668 10.119 3.5 8 3.5c-2.12 0-3.879 1.168-5.168 2.457A13.133 13.133 0 001.172 8z" clip-rule="evenodd"/>'
			      + '<path fill-rule="evenodd" d="M8 5.5a2.5 2.5 0 100 5 2.5 2.5 0 000-5zM4.5 8a3.5 3.5 0 117 0 3.5 3.5 0 01-7 0z" clip-rule="evenodd"/>'
			      + '</svg>';
			
 			var activateBtn =  '<svg class="bi bi-check-box" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">'
						  	 + '<path fill-rule="evenodd" d="M15.354 2.646a.5.5 0 010 .708l-7 7a.5.5 0 01-.708 0l-3-3a.5.5 0 11.708-.708L8 9.293l6.646-6.647a.5.5 0 01.708 0z" clip-rule="evenodd"/> '
					         + '<path fill-rule="evenodd" d="M1.5 13A1.5 1.5 0 003 14.5h10a1.5 1.5 0 001.5-1.5V8a.5.5 0 00-1 0v5a.5.5 0 01-.5.5H3a.5.5 0 01-.5-.5V3a.5.5 0 01.5-.5h8a.5.5 0 000-1H3A1.5 1.5 0 001.5 3v10z" clip-rule="evenodd"/>'
						     + '</svg>';
 				
 			var deactivateBtn = '<svg class="bi bi-x-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">'
 				 				+ '<path fill-rule="evenodd" d="M14 1H2a1 1 0 00-1 1v12a1 1 0 001 1h12a1 1 0 001-1V2a1 1 0 00-1-1zM2 0a2 2 0 00-2 2v12a2 2 0 002 2h12a2 2 0 002-2V2a2 2 0 00-2-2H2z" clip-rule="evenodd"/>'
 					  			+ '<path fill-rule="evenodd" d="M11.854 4.146a.5.5 0 010 .708l-7 7a.5.5 0 01-.708-.708l7-7a.5.5 0 01.708 0z" clip-rule="evenodd"/>'
			 					+ '<path fill-rule="evenodd" d="M4.146 4.146a.5.5 0 000 .708l7 7a.5.5 0 00.708-.708l-7-7a.5.5 0 00-.708 0z" clip-rule="evenodd"/>'
	 						    + '</svg>';
			
			$.each(data, function(index, value) {
				value.action = "<p class='my-auto actionColumn'>";
				if(value.status == "Inactive"){
					value.action += "<span class='p-1 mx-1 fontBtn activeBtn' id='" + value.seqid +"' onclick=activateAndDeactivateMember(this,1)>" + activateBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn viewBtn' id='" + value.seqid +"' onclick=getMemberDetails(this)>" + viewBtn + "</span>"
					value.status = "<div class='text-center'><span class='badge bg-warning text-uppercase'>" + value.status + "</span></div>"
				}
				else if(value.status == "Active"){
					value.action += "<span class='p-1 mx-1 fontBtn deactiveBtn' id='" + value.seqid +"' onclick=activateAndDeactivateMember(this,0)>" + deactivateBtn + "</span>";
					value.action += "<span class='p-1 mx-1 fontBtn viewBtn' id='" + value.seqid +"' onclick=getMemberDetails(this)>" + viewBtn + "</span>"
					
					value.status = "<div class='text-center'><span class='badge bg-primary text-uppercase'>" + value.status + "</span></div>"
				}
				else{
					value.action += "<span class='p-1 mx-1 fontBtn viewBtn' id='" + value.seqid +"' onclick=getMemberDetails(this)>" + viewBtn + "</span>"
					value.status = "<div class='text-center'><span class='badge bg-secondary text-uppercase'>" + value.status + "</span></div>"
				}
				value.action +="</p>"
			});
		}
		
		
		function getMemberDetails(element){
			var id = element.id
			
			if(id === undefined){
				bootbox.alert("Unable to extract the member ID from the selected member. Please try again later or refresh the page.");
				return;
			}
			clearMemberDetails();
			if(!$("#viewMember").hasClass("show")){
				$("#viewMember").modal("show");
			}
			
			$.ajax("api/admin/retrieveMemberInfo.json?memberId=" + id,{
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
					
					$("#viewMember .modal-body .data").each(function(index,element){
						var key = $(this).data('json-key');
			            if (key && data.result.hasOwnProperty(key)) {
			            	$(this).removeClass("placeholder")
			                $(this).text("	" + data.result[key] || "	-");
			            }
					});
				}
				else{
					$("#loading").hide();
					bootbox.alert(data.errorMsg,function(){
						$("#viewMember").modal("hide");
					});
				}
			})

		}
		
		function clearMemberDetails(){
			$("#viewMember .data").each(function(){
				$(this).text("");
				$(this).addClass("placeholder")
			})
		}
		
		
		function activateAndDeactivateMember(element,status){			
			if(status == 0 || status == 1){
				bootbox.confirm("Are you sure to change the member status to <b>" + (status == 1 ? "Active" : "Inactive") + "</b>?",function(result){
					if(result){
						var id = element.id
						
						var formData = {
							"seqid":id,
							"status":status
						}
						
						Notiflix.Loading.Dots('Processing...');															
						
						$.ajax("api/admin/updateMemberStatus.json",{
							method : "POST",
							accepts : "application/json",
							dataType : "json",
							contentType:"application/json; charset=utf-8",
							data: JSON.stringify(formData),
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
							Notiflix.Loading.Remove();
							if(data.errorMsg != null){
								var toast = createToast(data.errorMsg,"An attempt to update member status <b>Failed</b>",false);
							}
							else{
								var toast = createToast(data.result,"An attempt to update member status <b>Success</b>",true);
								readyFunction();
							}
						});
					}
				})	
			}
			else{
				bootbox.alert("Invalid request found. PLease try again later.")
			}
		}
	</script>
</body>

</html>
