Vue.component("komentari-prodavac", {
	data: function () {
		    return {
				komentari: {}
		    }
	},
	template: ` 
<div>
		<h1>Prikaz komentara prodavac</h1>
		
		<table border = 1>
		<tr> <th>Kupac</th> <th>Manifestacija</th> <th>Tekst</th> <th>Ocjena</th> </tr>
		
		<tr v-for="k in komentari" >
			<td>{{k.kupac.ime}} {{k.kupac.prezime}}</td>
			<td>{{k.manifestacija.naziv}}</td>
			<td>{{k.tekst}}</td>
			<td>{{k.ocena}}</td>
			<td v-bind:hidden="k.odobren === true" ><input type="button" v-on:click="odobri(k)" value="Odobri" /></td>
		</tr>
		
		</table>
	
</div>		  
`
	, 
	methods : {
		init : function() {
			let self = this;
			$.ajax({
				url: "/rest/comments/currentSeller",
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
		odobri: function(k){
			let self = this;
			$.ajax({
				url: "/rest/comments/approve/" + k.id,
				method: "PUT",
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
		let flag = 0;
		$.ajax({
			url: "/rest/users/currentUser",
			method: "GET",
			success: function(data){
				if(data === null || data.uloga != "PRODAVAC"){
					window.location.href = "#/login";
					flag = 1;
				}
			}
		});
		if(flag == 1){
			return;
		}
        this.init();
    }
});