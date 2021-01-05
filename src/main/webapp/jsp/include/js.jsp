
<script src="<spring:url value='/plugins/bootstrap/js/bootstrap.bundle.min.js'/>"></script>

<script type="text/javascript">

<%-- Trigger dropdown-toggle button function --%>
$('.dropdown-toggle').dropdown();

$('#logout-link').on('click', function() {
	$('#logout-form').submit();
	return false;
});

</script>