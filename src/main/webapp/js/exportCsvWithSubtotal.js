function exportTableToCSV($table, filename) {

		    var $rows = $table.find('tr:has(td),tr:has(th)'),

		        // Temporary delimiter characters unlikely to be typed by keyboard
		        // This is to avoid accidentally splitting the actual contents
		        tmpColDelim = String.fromCharCode(11), // vertical tab character
		        tmpRowDelim = String.fromCharCode(0), // null character

		        // actual delimiter characters for CSV format
		        colDelim = '","',
		        rowDelim = '"\r\n"',

		        // Grab text from table into CSV formatted string
		        csv = '"' + $rows.map(function (i, row) {
		            var $row = $(row), $cols = $row.find('td,th');

		            return $cols.map(function (j, col) {
		                var $col = $(col), text = $col.text();

		                return text.replace(/"/g, '""'); // escape double quotes

		            }).get().join(tmpColDelim);

		        }).get().join(tmpRowDelim)
		            .split(tmpRowDelim).join(rowDelim)
		            .split(tmpColDelim).join(colDelim) + '"',



		        // Data URI
		        csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(csv);

		        console.log(csv);
		    		        
		        
		        download_csv(csv, filename);
		}

		
		function download_csv(csv, filename) {
		    var csvFile;
		    var downloadLink;

		    // CSV FILE
		    csvFile = new Blob([csv], {type: "text/csv"});

		    // Download link
		    downloadLink = document.createElement("a");

		    // File name
		    downloadLink.download = filename;

		    // We have to create a link to the file
		    downloadLink.href = window.URL.createObjectURL(csvFile);

		    // Make sure that the link is not displayed
		    downloadLink.style.display = "none";

		    // Add the link to your DOM
		    document.body.appendChild(downloadLink);

		    // Lanzamos
		    downloadLink.click();
		}