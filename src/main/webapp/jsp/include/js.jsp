<script src="<spring:url value='/plugins/bootstrap/js/bootstrap.bundle.min.js'/>"></script>
<script src="<spring:url value='/plugins/jquery-easing/jquery.easing.min.js'/>"></script>
<script src="<spring:url value='/js/template.js'/>"></script>

<script type="text/javascript">


$('#logOutBtn').on('click', function() {
	$('#logout-form').submit();
	return false;
});

</script>