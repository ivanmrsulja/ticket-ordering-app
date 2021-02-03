Vue.component("komentari-admin", {
	data: function () {
		    return {
		    komentari: {}
		    }
	},
	template: ` 
<div style="width:60%" >
		<h1>Prikaz komentara admin</h1>
		
		<table class="table table-hover" >
		<tr style="background-color : lightgray"> <th>Kupac</th> <th>Manifestacija</th> <th>Tekst</th> <th>Ocjena</th> <th>Akcija</th> </tr>
		
		<tr v-for="k in komentari" >
			<td>{{k.kupac.ime}} {{k.kupac.prezime}}</td>
			<td>{{k.manifestacija.naziv}}</td>
			<td>{{k.tekst}}</td>
			<td>{{k.ocena}}</td>
			<td v-bind:hidden="k.obrisan === true" ><input type="button" class="btn btn-danger" v-on:click="obrisi(k)" value="Obrisi" /></td>
		</tr>
		
		</table>
	
</div>		  
`
	, 
	methods : {
		init : function() {
			let self = this;
			$.ajax({
				url: "/rest/comments/allComments",
				method: "GET",
				success: function(data){
					for(d of data){
						d.tekst = d.tekst.replaceAll("_", " ");
					}
					self.komentari = data;
				},
				error: function(response){
					alert("Doslo je do greske.");
				}
			});
		},
		obrisi: function(k){
			let self = this;
			$.ajax({
				url: "/rest/comments/delete/"+k.id,
				method: "DELETE",
				success: function(data){
					self.komentari = data;
				},
				error: function(response){
					alert("Doslo je do greske.");
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
        this.init();
    }
});