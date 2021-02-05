Vue.component("karte-prodavac", {
	data: function () {
		    return {
		    	karte: {}
		    }
	},
	template: ` 
<div style="width : 80%" >
		<h1>Prikaz karata prodavac</h1>
		
		<h2 v-bind:hidden="Object.keys(this.karte).length != 0" >Trenutno nemate rezervisanih karata...</h2>
		
		</br>
		
		<table class="table table-hover" v-bind:hidden="Object.keys(this.karte).length == 0">
			<tr bgcolor="lightgrey">
				<th>Kupac</th>
				<th>Ime i prezime</th>
				<th>Tip</th>
				<th>Broj mesta</th>
				<th>Status</th>
				<th>Manifestacija</th>
				<th>Datum manifestacije</th>
			</tr>
	
			<tr v-for="k in this.karte">
				<td>{{k.idKupca}}</td>
				<td>{{k.imePrezime}}</td>
				<td>{{k.tip}}</td>
				<td>{{k.brojMesta}}</td>
				<td>{{k.status}}</td>
				<td>{{k.idManifestacije}}</td>
				<td>{{k.datum}}</td>
			</tr>
		</table>
		
</div>		  
`
	, 
	methods : {
		init : function() {
		}
	},
	mounted () {
		$.ajax({
			url: "/rest/users/currentUser",
			method: "GET",
			success: function(data){
				if(data === null || data.uloga != "PRODAVAC"){
					window.location.href = "#/login";
				}
			}
		});
		
		let self = this;
        $.get("/rest/tickets/prodavac", function(data){
        	
        	for(d of data){
        		var now = new Date(d.datum);

				var day = ("0" + now.getDate()).slice(-2);
				var month = ("0" + (now.getMonth() + 1)).slice(-2);
				
				var today = now.getFullYear()+"-"+(month)+"-"+(day) + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
				d.datum = today;
        	}
        	
        	self.karte = data;
        });
    }
});