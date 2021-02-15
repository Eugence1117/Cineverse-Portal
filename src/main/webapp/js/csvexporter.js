/* Copyright (c) Financial Link Sdn Bhd */
(function($) {
	var COLUMN_DELIMITER = ',';
	var ROW_DELIMITER = '\r\n';
	
	CSVExporter = {
			save: function(button, fileName, theadNode, rowNodes) {
				var csvData = generateCSV(theadNode, rowNodes);
				
				var blob = new Blob([csvData], { type: 'text/csv;charset=utf-8'	});
				
				if (window.navigator.msSaveOrOpenBlob) {
					// IE 10+
					window.navigator.msSaveBlob(blob, fileName);
				} else {
					var url = window.URL.createObjectURL(blob);
					var a = document.createElement("a");
					a.style = "display: none";
					a.href = url;
					a.download = fileName;
					document.body.appendChild(a);
					a.click();
					setTimeout(function(){
						document.body.removeChild(a);
						window.URL.revokeObjectURL(url);
					}, 500);
				}
			}
		}
	
	var generateCSV = function(theadNode, rowNodes) {
		var csv = "";
		
		$(theadNode).children('th').each(function(index, element) {
			csv += '"' + stripHtml(element) + '"' + COLUMN_DELIMITER;
		});
		csv += ROW_DELIMITER;
		
		$.each(rowNodes, function (index, rowNode) {
			$(rowNode).children('td').each(function(index, element) {
				var data = stripHtml(element);
				if (!isNaN(data)) {
            		csv += "=";
				} else if (isMoney(data)) {
					data = data.replace(MONEY_PREFIX, '');
				}
            	csv += '"' + data + '"' + COLUMN_DELIMITER;
			});
			csv += ROW_DELIMITER;
		});
		
		return csv;
	}
	
	
	
	QueryExporter = {
			save: function(button, fileName, theadNode, rowNodes) {
				
				var	csvData = generateQueryReport(theadNode, rowNodes);
				
				var blob = new Blob([csvData], { type: 'text/csv;charset=utf-8'	});
				
				if (window.navigator.msSaveOrOpenBlob) {
					// IE 10+
					window.navigator.msSaveBlob(blob, fileName);
				} else {
					var url = window.URL.createObjectURL(blob);
					var a = document.createElement("a");
					a.style = "display: none";
					a.href = url;
					a.download = fileName;
					document.body.appendChild(a);
					a.click();
					setTimeout(function(){
						document.body.removeChild(a);
						window.URL.revokeObjectURL(url);
					}, 500);
				}
			}
		}
	
	var generateQueryReport = function(theadNode, rowNodes){
		var csv = "";
		var count = 0;
		
		$.each(rowNodes, function (index, rowNode) {
			$(rowNode).children('td').each(function(index, element) {
				count++;
				var data = stripHtml(element);
				if (isMoney(data)) {
					data = data.replace(MONEY_PREFIX, '');
				}
            	csv += '"' + data + '"' + COLUMN_DELIMITER;
            	if( (count%2) == 0){
            		csv += '"' + "Y" + '"' + COLUMN_DELIMITER;
            		return false;
            	}
			});
			csv += ROW_DELIMITER;
		});
		
		return csv;
	}
	
	
	
	var stripHtml = function(element) {
		return $(element).text().replace(/"/g, '""');
	}
	
	var MONEY_PREFIX = "S$";
	var isMoney = function(input) {
		if (input.indexOf(MONEY_PREFIX) === -1)
			return false;
		var value = input.replace(MONEY_PREFIX, '');
		return !isNaN(value);
	}

})(jQuery);