Vue.component("karte-kupac", {
	data: function () {
		    return {
				karte: {}
		    }
	},
	template: ` 
<div>
		<h1>Moje karte:</h1>
		
		<h2 v-bind:hidden="Object.keys(this.karte).length != 0" >Trenutno nemate rezervisanih karata...</h2>
		
		<table border=1 v-bind:hidden="Object.keys(this.karte).length == 0">
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
				<td><input type="button" value="Odustani" v-on:click="odustani(k)" v-bind:disabled="(new Date(k.datum.split(' ')[0].split('-')[2]+'-'+k.datum.split(' ')[0].split('-')[1]+'-'+k.datum.split(' ')[0].split('-')[0])).getTime() < (Date.now() + 604800000)" /></td>
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
					alert("Uspesno azurirano.");
			        $.get("/rest/tickets/forUser", function(data){
						for(d of data){
							let now = new Date(parseInt(d.datum));
							let day = ("0" + now.getDate()).slice(-2);
							let month = ("0" + (now.getMonth() + 1)).slice(-2);
							let today = (day) +"-"+(month)+"-"+ now.getFullYear() + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
							
							d.datum = today;
						}
						self.karte = data;
					});
				}
			});
		},
		init: function(){
			let self = this;
	        $.get("/rest/tickets/forUser", function(data){
				for(d of data){
					let now = new Date(parseInt(d.datum));
					let day = ("0" + now.getDate()).slice(-2);
					let month = ("0" + (now.getMonth() + 1)).slice(-2);
					let today = (day) +"-"+(month)+"-"+ now.getFullYear() + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
					
					d.datum = today;
				}
				self.karte = data;
			});
		} 
	},
	mounted () {
		$.ajax({
			url: "/rest/users/currentUser",
			method: "GET",
			success: function(data){
				if(data === null || data.uloga != "KUPAC"){
					window.location.href = "#/login";
				}
			}
		});
		this.init();
    }
});