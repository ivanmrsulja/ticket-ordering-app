Vue.component("manifestacije-admin", {
	data: function () {
		    return {
		    	manifestacije: {}
		    }
	},
	template: ` 
<div style="width:80%" >
		<h1>Prikaz manifestacija admin</h1>
		
		<table class="table table-hover">
			<tr bgcolor="lightgrey">
				<th>Naziv</th>
				<th>Tip manifestacije</th>
				<th>Datum odrzavanja</th>
				<th>Broj mijesta</th>
				<th>Cijena karte</th>
				<th>Adresa odrzavanja</th>
				<th>Status</th>
				<th colspan=2 >Akcije</th>
			</tr>
	
			<tr v-for="m in this.manifestacije">
				<td>{{m.naziv}}</td>
				<td>{{m.tipManifestacije}}</td>
				<td>{{m.datumOdrzavanja}}</td>
				<td>{{m.brojMesta}}</td>
				<td>{{m.cenaRegular}}</td>
				<td>{{m.lokacija.adresa}}</td>
				<td>{{m.status}}</td>
				<td><input type="button" class="btn btn-danger" value="Odobri" v-on:click="odobri(m)" v-bind:disabled="m.status == 'AKTIVNO'" /></td>
				<td><input type="button" class="btn btn-danger" value="Obrisi" v-on:click="obrisi(m)" /></td>
			</tr>
		</table>
		<br/>
		<br/>
</div>		  
`
	, 
	methods : {
		selectManifestacija : function(m) {
		
		}, 
		odobri : function (m) {
			let self = this;
			$.get("/rest/manifestations/odobri/" + m.id + "", function(data){
				
				$.get("/rest/manifestations/all", function(data){
		        	for(d of data){
		        		var now = new Date(d.datumOdrzavanja);
		
						var day = ("0" + now.getDate()).slice(-2);
						var month = ("0" + (now.getMonth() + 1)).slice(-2);
						
						var today = (day)+"-"+(month)+"-"+ now.getFullYear() + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
						d.datumOdrzavanja = today;
		        	}
		        	
		        	self.manifestacije = data;
		        	
		        });
				
			});
		},
		obrisi: function(m){
			let self = this;
			
			$.ajax({
				url: "/rest/manifestations/obrisi/" + m.id + "",
				type: 'DELETE',
				success: function(data){
					$.get("/rest/manifestations/all", function(data){
		        	for(d of data){
		        		var now = new Date(d.datumOdrzavanja);
		
						var day = ("0" + now.getDate()).slice(-2);
						var month = ("0" + (now.getMonth() + 1)).slice(-2);
						
						var today = (day)+"-"+(month)+"-"+ now.getFullYear() + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
						d.datumOdrzavanja = today;
		        	}
		        	
		        	self.manifestacije = data;
		        	
		        });
				}
			});
		}
	},
	mounted () {
		$.ajax({
			url: "/rest/users/currentUser",
			method: "GET",
			success: function(data){
				if(data === null || data.uloga != "ADMIN"){
					window.location.href = "#/login";
				}
			}
		});
		let self = this;
        $.get("/rest/manifestations/all", function(data){
        	for(d of data){
        		var now = new Date(d.datumOdrzavanja);

				var day = ("0" + now.getDate()).slice(-2);
				var month = ("0" + (now.getMonth() + 1)).slice(-2);
				
				var today = (day)+"-"+(month)+"-"+ now.getFullYear() + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
				d.datumOdrzavanja = today;
        	}
        	
        	self.manifestacije = data;
        	
        });
    }
});