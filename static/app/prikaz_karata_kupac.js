Vue.component("karte-kupac", {
	data: function () {
		    return {
				karte: {}
		    }
	},
	template: ` 
<div>
		<h1>Moje karte:</h1>
		
		<table border=1>
			<tr bgcolor="lightgrey">
				<th>Manifestacija</th>
				<th>Broj mesta</th>
				<th>Tip karte</th>
				<th>Datum</th>
			</tr>
	
			<tr v-for="k in this.karte">
				<td>{{k.idManifestacije}}</td>
				<td>{{k.brojMesta}}</td>
				<td>{{k.tip}}</td>
				<td>{{k.datum}}</td>
				<td><input type="button" value="Odustani" v-on:click="odustani(k)" /></td>
			</tr>
		</table>
		
</div>		  
`
	, 
	methods : {
		odustani : function(k) {
			let self = this;
			$.ajax({
				url: '/rest/tickets/odustanak/' + k.id,
				type: 'PUT',
				success: function(resp){
					toast("Uspesno azurirano.");
			        $.get("/rest/tickets/forUser", function(data){
						for(d of data){
							let now = new Date(parseInt(d.datum));
							let day = ("0" + now.getDate()).slice(-2);
							let month = ("0" + (now.getMonth() + 1)).slice(-2);
							let today = now.getFullYear()+"-"+(month)+"-"+(day) + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
							
							d.datum = today;
						}
						self.karte = data;
					});
				}
			});
		}, 
	},
	mounted () {
		let self = this;
        $.get("/rest/tickets/forUser", function(data){
			for(d of data){
				let now = new Date(parseInt(d.datum));
				let day = ("0" + now.getDate()).slice(-2);
				let month = ("0" + (now.getMonth() + 1)).slice(-2);
				let today = now.getFullYear()+"-"+(month)+"-"+(day) + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
				
				d.datum = today;
			}
			self.karte = data;
		});
    }
});