Vue.component("komentari-prodavac", {
	data: function () {
		    return {
				komentari: {}
		    }
	},
	template: ` 
<div style="width:60%">
		<h1 style="margin-bottom : 20px">Prikaz komentara prodavac</h1>
		
		<h1 v-bind:hidden="Object.keys(this.komentari).length != 0" style="margin-top : 50px; color : gray" >Trenutno nema komentara ni na jednoj od vasih manifestacija.</h1>
		
		<table class="table table-hover" v-bind:hidden="Object.keys(this.komentari).length == 0">
		<tr style="background-color : lightgray" > <th>Kupac</th> <th>Manifestacija</th> <th>Tekst</th> <th>Ocjena</th> <th>Status</th> </tr>
		
		<tr v-for="k in komentari" >
			<td>{{k.kupac.ime}} {{k.kupac.prezime}}</td>
			<td>{{k.manifestacija.naziv}}</td>
			<td>{{k.tekst}}</td>
			<td>{{k.ocena}}</td>
			<td v-bind:hidden="k.odobren === true" ><input type="button" class="btn btn-success" v-on:click="odobri(k)" value="Odobri" /></td>
			<td v-bind:hidden="k.odobren !== true" ><h4 style="color:lightgray">ODOBREN</h4></td>
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