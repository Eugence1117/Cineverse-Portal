/**
 * 
 */

function injectData(index,synopsis,id,ea,date,time,lang,distributor,director,cast,censorship,type){
var html = 
	'<div class="row input-material form-group"> ' +
    '<textarea id="synopsis['+index+']" class="form-control floatLabel text-center border-0" style="color:black;resize:none">'+synopsis+'</textarea>' +
	'<label for="synopsis">Synopsis</label>' +
	'</div>' +
	'<div class="row">' +
	'<div class="input-material form-group col-sm-6">' +
	'<input id="movieId['+index+']" type="text" class="form-control floatLabel data" value='+id+' readonly>' +
	'<label for="movieId">Movie ID</label>' +
	'</div>' +
    '<div class="input-material form-group col-sm-6">' +
    '<input id="earlyAccess['+index+']" type="text" class="form-control floatLabel data" value='+ ea +' readonly>' +
    '<label for="earlyAccess">Early Access</label>' +
	'</div>' +
    '</div>' +
    '<div class="row">' +
	'<div class="input-material form-group col-sm-6">' +
	'<input id="releaseDate['+index+']" type="text" class="form-control floatLabel data" value='+ date +' readonly>' +
	'<label for="releaseDate">Release Date</label>' +
	'</div>' +
	'<div class="input-material form-group col-sm-6">' +
	'<input id="totalTime['+index+']" type="text" class="form-control floatLabel data" value=' + time + ' readonly>' +
	'<label for="totalTime">Total Time</label>' +
	'</div>' +
    '</div>' +
    '<div class="row">' +
	'<div class="input-material form-group col-sm-6">' +
	'<input id="language['+index+']" type="text" class="form-control floatLabel data" value='+ lang +' readonly>' +
	'<label for="language">Language</label>' +
	'</div>' +
	'<div class="input-material form-group col-sm-6">' +
	'<input id="distributor['+index+']" type="text" class="form-control floatLabel data" value='+ distributor +' readonly>' +
	'<label for="distributor">Distributor</label>' +
	'</div>' +
    '</div>' +    
    '<div class="row">' +
	'<div class="input-material form-group col-sm-6">' +
	'<input id="director['+index+']" type="text" class="form-control floatLabel data" value='+ director +' readonly>' +
	'<label for="director">Director</label>' +
	'</div>' +
	'<div class="input-material form-group col-sm-6">' +
	'<input id="cast['+index+']" type="text" class="form-control floatLabel data" value='+  cast +' readonly>' +
	'<label for="cast">Cast</label>' +
	'</div>' +
    '</div>' +
    '<div class="row">' +
	'<div class="input-material form-group col-sm-6">' +
	'<input id="censorship['+index+']" type="text" class="form-control floatLabel data" value='+ censorship +' readonly>' +
	'<label for="censorship">Censorship</label>' +
	'</div>' +
	'<div class="input-material form-group col-sm-6">' +
	'<input id="type['+index+']" type="text" class="form-control floatLabel data" value='+ type +' readonly>' +
	'<label for="type">Genre</label>' +
	'</div>' +
    '</div>'
	
	return html;
}