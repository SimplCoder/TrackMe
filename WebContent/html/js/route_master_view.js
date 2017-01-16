function addLocation()
{
	var  location = document.getElementById("location");
	var  table = document.getElementById("entrydata");
	var rowCount = document.getElementById('entrydata').rows.length;
	row = table.insertRow( rowCount );
	row.className = "leftMenu";
	cell = row.insertCell(0);
	cell.innerHTML = rowCount;
	cell = row.insertCell(1);
	var input = document.createElement('input');
	input.type = "input";
	input.value = location.value;
	input.id="route"+rowCount;
	input.name="route"+rowCount;
	input.readOnly = true;
	cell.appendChild(input);
	
	cell = row.insertCell(2);
	var btn = document.createElement('input');
	btn.type = "button";
	btn.className = "button";
	btn.value = "Delete";
	btn.onclick = ( 
		function( ) 
			{
				this.parentNode.parentNode.parentNode.removeChild(this.parentNode.parentNode);
				var rows = document.getElementById("rows");
				rows.value = document.getElementById("rows").value - 1;
			}
	);
	cell.appendChild(btn);
	
	var rows = document.getElementById("rows");
	rows.value = rowCount;
}

function getData( id )
{
	var xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.open("get", "/TrackMeWeb/TrackMeServlet?action=getLocationsForRoute&id="+id, true);
	xmlHttpRequest.onreadystatechange = addDataToPage;
	xmlHttpRequest.send(null);
	
	function addDataToPage()
	{
	if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
		{
		
		var table = document.getElementById("entrydata");
		var rowCount = document.getElementById('entrydata').rows.length;
		var dom = (new DOMParser()).parseFromString(xmlHttpRequest.responseText, "text/xml");
		var data = dom.getElementsByTagName("route");
		var i = 0;
		while(i < data.length )
			{
				var rowCount = document.getElementById('entrydata').rows.length;
				row = table.insertRow( rowCount );
				row.className = "leftMenu";
				cell = row.insertCell(0);
				cell.innerHTML = rowCount;
				cell = row.insertCell(1);
				var input = document.createElement('input');
				input.type = "input";
				input.value = data[i].firstChild.nodeValue;
				input.id="route"+rowCount;
				input.name="route"+rowCount;
				input.readOnly = true;
				cell.appendChild(input);
				
				cell = row.insertCell(2);
				var btn = document.createElement('input');
				btn.type = "button";
				btn.className = "button";
				btn.value = "Delete";
				btn.buttonValue = id;
				btn.onclick = ( 
					function( id ) 
						{
							this.parentNode.parentNode.parentNode.removeChild(this.parentNode.parentNode);
							var rows = document.getElementById("rows");
							rows.value = document.getElementById("rows").value - 1;
					}
				);
				cell.appendChild(btn);
				
				var rows = document.getElementById("rows");
				rows.value = rowCount;
				i++;
			}
		}
	}
}

var xmlHttpRequest = new XMLHttpRequest();
function getLocations()
{
	xmlHttpRequest.open("get", "/TrackMeWeb/TrackMeServlet?action=getLocations", true);
	xmlHttpRequest.onreadystatechange = createDropdown;
	xmlHttpRequest.send(null);
}
function createDropdown()
{

	if( xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200 )
	{
		var loaded = document.getElementById("loaded").value;
		if( document.getElementById("location").length == 1)
		{
			var dom = (new DOMParser()).parseFromString(xmlHttpRequest.responseText, "text/xml");
			var data = dom.getElementsByTagName("location");
			var i=0;
			var select = document.getElementById("location");
			
			select.innerHtml = "";
			while( i < data.length )
				{
					var option = document.createElement('option');
					option.value = data[i].childNodes[0].firstChild.nodeValue;
					option.text = data[i].childNodes[0].firstChild.nodeValue;
					
					select.add(option);
					i++;
				}
			loaded = "true";
		}
	}
}

function loadData() 
{
	var xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.open("get", "/TrackMeWeb/TrackMeServlet?action=getRoutes", true);
	xmlHttpRequest.onreadystatechange = addDataToPage;
	xmlHttpRequest.send(null);
	
	function addDataToPage()
	{
	if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
		{
		
		var table = document.getElementById("entrydata");
		var dom = (new DOMParser()).parseFromString(xmlHttpRequest.responseText, "text/xml");
		var data = dom.getElementsByTagName("route");
		var i = 0;
		while(i < data.length )
			{
			var id = data[i].childNodes[0].firstChild.nodeValue;
			row = table.insertRow( i+1 );
			row.className="leftMenu";
			cell = row.insertCell(0);
			var btn = document.createElement('input');
			btn.type = "button";
			btn.className = "button";
			btn.value = "Edit";
			btn.buttonValue = id;
			btn.action = "editRoute";
			btn.onclick = ( 
				function( id ) 
					{
					var xmlHttpRequest = new XMLHttpRequest();
					xmlHttpRequest.open("get", "/TrackMeWeb/TrackMeServlet?action=editRoute&id="+id.srcElement.buttonValue, true);
					xmlHttpRequest.onreadystatechange = addDataEditVehicle;
					xmlHttpRequest.send(null);
					function addDataEditVehicle()
					{
						if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200)
						{
							var editVehicle = document.getElementById("editRoute");
							editVehicle.innerHTML = xmlHttpRequest.responseText;
							getData( id.srcElement.buttonValue );
						}
					}
				}
				
					
			);
			cell.appendChild(btn);
			cell = row.insertCell(1);
			cell.innerHTML = data[i].childNodes[0].firstChild == null ? "" :data[i].childNodes[0].firstChild.nodeValue;
			cell = row.insertCell(2);
			cell.innerHTML = data[i].childNodes[1].firstChild == null ? "" :data[i].childNodes[1].firstChild.nodeValue;
			cell = row.insertCell(3);
			cell.innerHTML = data[i].childNodes[2].firstChild == null ? "" :data[i].childNodes[2].firstChild.nodeValue;
			cell = row.insertCell(4);
			cell.innerHTML = data[i].childNodes[3].firstChild == null ? "" :data[i].childNodes[3].firstChild.nodeValue;
			cell = row.insertCell(5);
			cell.innerHTML = data[i].childNodes[4].firstChild == null ? "" :data[i].childNodes[4].firstChild.nodeValue;
			cell = row.insertCell(6);
			cell.innerHTML = data[i].childNodes[5].firstChild == null ? "" :data[i].childNodes[5].firstChild.nodeValue;
			cell = row.insertCell(7);
			var btn = document.createElement('input');
			btn.type = "button";
			btn.className = "button";
			btn.value = "Delete";
			btn.buttonValue = id;
			btn.action = "deleteRoute";
			btn.onclick = ( 
				function( id ) 
					{
					if( confirm('Are you sure, Route no ['+id.srcElement.buttonValue+'] will be deleted !'))
						{
						  var row = id.currentTarget.parentNode.parentNode;
						  row.parentNode.removeChild(row);
						var xmlHttpRequest = new XMLHttpRequest();
						xmlHttpRequest.open("get", "/TrackMeWeb/TrackMeServlet?action=deleteRoute&id="+id.srcElement.buttonValue, true);
						xmlHttpRequest.onreadystatechange = addData;
						xmlHttpRequest.send(null);
						 function addData()
						{
							  var row = id.parentNode.parentNode;
							  row.parentNode.removeChild(row);
						} 
					}
					
				}
				
					
			);
			cell.appendChild(btn);
			i++;
			}
		}
	}
}