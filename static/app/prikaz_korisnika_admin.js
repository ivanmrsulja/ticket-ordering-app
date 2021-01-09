Vue.component("korisnici-admin", {
	data: function () {
		    return {
		    	korisnici: {}
		    }
	},
	template: ` 
<div>
		<h1>Prikaz korisnika admin</h1>
		
		<table border=1>
			<tr bgcolor="lightgrey">
				<th>Username</th>
				<th>Password</th>
				<th>Ime</th>
				<th>Prezime</th>
				<th>Pol</th>
				<th>Datum rodjenja</th>
				<th>Uloga</th>
			</tr>
	
			<tr v-for="k in this.korisnici">
				<td>{{k.username}}</td>
				<td>{{k.password}}</td>
				<td>{{k.ime}}</td>
				<td>{{k.prezime}}</td>
				<td>{{k.pol}}</td>
				<td>{{k.datumRodjenja}}</td>
				<td>{{k.uloga}}</td>
				<td><input type="button" value="Obrisi" v-on:click="obrisi(k)" /></td>
				<td v-if="!k.banovan" v-bind:hidden="k.uloga == 'ADMIN'"><input type="button" value="Banuj" v-on:click="banuj(k)" /></td>
				<td v-else v-bind:hidden="k.uloga == 'ADMIN'"><input type="button" value="Unbanuj" v-on:click="banuj(k)" /></td>
			</tr>
		</table>
	
</div>		  
`
	, 
	methods : {
		init : function(){
			let self = this;
	        $.get("/rest/users/all", function(data){
	        	
	        	for(d of data){
	        		var now = new Date(d.datumRodjenja);
	
					var day = ("0" + now.getDate()).slice(-2);
					var month = ("0" + (now.getMonth() + 1)).slice(-2);
					
					var today = now.getFullYear()+"-"+(month)+"-"+(day) + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
					d.datumRodjenja = today;
	        	}
	        	
	        	self.korisnici = data;
	        });
		},
		banuj : function(k){
			let self = this;
			$.ajax({
				url: "/rest/users/" + k.username,
				method: "PUT",
				contentType: "application/json",
				success: function(response){ toast(response); self.init(); },
				error: function(response){ toast("Doslo je do greske.")}
			});
		},
		obrisi : function(k) {
			let self = this;
			$.ajax({
				url: "/rest/users/" + k.username,
				type: "DELETE",
				success: function(data){
					toast("Uspesno azurirano.")
					$.get("/rest/users/all", function(data){
        	
			        	for(d of data){
			        		var now = new Date(d.datumRodjenja);
			
							var day = ("0" + now.getDate()).slice(-2);
							var month = ("0" + (now.getMonth() + 1)).slice(-2);
							
							var today = now.getFullYear()+"-"+(month)+"-"+(day) + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
							d.datumRodjenja = today;
			        	}
			        	
			        	self.korisnici = data;
			        });
				},
				error: function(data){
					toast("Doslo je do greske")
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