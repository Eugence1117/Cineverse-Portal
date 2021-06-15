<script src="<spring:url value='/plugins/bootstrap/js/bootstrap.bundle.min.js'/>"></script>
<script src="<spring:url value='/plugins/jquery-easing/jquery.easing.min.js'/>"></script>
<script type="text/javascript" src="<spring:url value='/plugins/momentjs/moment.js'/>"></script>
<script src="<spring:url value='/js/template.js'/>"></script>
<script src="<spring:url value='/js/toastMessageInitiater.js'/>"></script>

<script type="text/javascript">


$('#logOutBtn').on('click', function() {
	window.sessionStorage.clear();
	$('#logout-form').submit();
	return false;
});

$(document).ready(function(){
	loadContent();
});

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

</script>