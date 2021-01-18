Vue.component("korisnici-admin", {
	data: function () {
		    return {
		    	korisnici: {},
		    	searchParams : {ime : "", prezime: "", username: "", uloga: "KUPAC", tip: "NOVI", kriterijumSortiranja: "IME", opadajuce: false}
		    }
	},
	template: ` 
<div style="width: 80%">
		<h1>Prikaz korisnika admin</h1>
		
		<br/>
		
		<div id="mySidebar" class="sidebar">
		  <a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
		  <table>
			  <tr><td colspan=2 ><input type="text" name="ime" placeholder="Unesite ime korisnika" v-model="searchParams.ime" /></td></tr>
			  <tr><td colspan=2 ><input type="text" name="prezime" placeholder="Unesite prezime korisnika" v-model="searchParams.prezime" /></td></tr>
			  <tr><td colspan=2 ><input type="text" name="username" placeholder="Unesite username korisnika" v-model="searchParams.username" /></td></tr>
			  
			  <tr><td style="color:white">Uloga korisnika:</td> 
			  		<td>
				  		<select name="uloga" id="uloga" v-model="searchParams.uloga" >
				  		  <option value="SVE">SVE</option>
						  <option value="KUPAC">KUPAC</option>
						  <option value="PRODAVAC">PRODAVAC</option>
						</select>
					</td></tr>
			  
			  <tr><td style="color:white">Tip korisnika:</td> 
			  		<td>
				  		<select name="tip" id="tip" v-model="searchParams.tip" >
				  		  <option value="SVE">SVE</option>
						  <option value="NOVI">NOVI</option>
						  <option value="REDOVAN">REDOVAN</option>
						  <option value="PREMIUM">PREMIUM</option>
						</select>
					</td></tr>
			  <tr><td style="color:white">Sortiraj po:</td>
			  		<td>
				  		<select name="kriterijum" id="kriterijum" v-model="searchParams.kriterijumSortiranja" >
						  <option value="IME">IME</option>
						  <option value="PREZIME">PREZIME</option>
						  <option value="USERNAME">USERNAME</option>
						  <option value="BODOVI">BODOVI</option>
						</select>
					</td></tr>
			  <tr><td style="color:white">Sortiraj opadajuce:</td> <td><input type="checkbox" name="opadajuce" v-model="searchParams.opadajuce" ></td></tr>
			  <tr><td colspan=2 align=center ><input type="button" name="search" value="Pretrazi" v-on:click="pretraga()" /></td></tr>
		  </table>
		</div>
		
		<div id="main">
		  <button class="openbtn" onclick="openNav()">&#9776; Pretraga</button>
		</div>
		
		<br/>
		
		<table class="table table-hover">
			<tr bgcolor="lightgrey">
				<th>Username</th>
				<th>Password</th>
				<th>Ime</th>
				<th>Prezime</th>
				<th>Pol</th>
				<th>Datum rodjenja</th>
				<th>Uloga</th>
			</tr>
	
			<tr v-for="k in this.korisnici" v-bind:class="{ sumnjiv: k.brojOtkazivanja >= 5 }">
				<td>{{k.username}}</td>
				<td>{{k.password}}</td>
				<td>{{k.ime}}</td>
				<td>{{k.prezime}}</td>
				<td>{{k.pol}}</td>
				<td>{{k.datumRodjenja}}</td>
				<td>{{k.uloga}}</td>
				<td><input type="button" value="Obrisi" v-on:click="obrisi(k)" v-bind:hidden="k.obrisan || k.uloga == 'ADMIN' " /></td>
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
		pretraga : function(){
			let self = this;
			axios
			.get("/rest/users/pretraga?ime="+this.searchParams.ime + "&prezime="+this.searchParams.prezime + "&username="+this.searchParams.username + "&tip="+this.searchParams.tip + "&uloga="+this.searchParams.uloga + "&kriterijumSortiranja="+this.searchParams.kriterijumSortiranja + "&opadajuce="+this.searchParams.opadajuce)
			.then(function(response){
				for(d of response.data){
	        		var now = new Date(d.datumRodjenja);
	
					var day = ("0" + now.getDate()).slice(-2);
					var month = ("0" + (now.getMonth() + 1)).slice(-2);
					
					var today = now.getFullYear()+"-"+(month)+"-"+(day) + " " + ("0" + (now.getHours())).slice(-2) + ":" + ("0" + (now.getMinutes())).slice(-2);
					d.datumRodjenja = today;
	        	}
	        	
	        	self.korisnici = response.data;
			})
			.catch(function(response){alert("Doslo je do greske.")});
		},
		banuj : function(k){
			let self = this;
			$.ajax({
				url: "/rest/users/" + k.username,
				method: "PUT",
				contentType: "application/json",
				success: function(response){ alert(response); self.init(); },
				error: function(response){ alert("Doslo je do greske.")}
			});
		},
		obrisi : function(k) {
			let self = this;
			$.ajax({
				url: "/rest/users/" + k.username,
				type: "DELETE",
				success: function(data){
					alert("Uspesno azurirano.")
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
					alert("Doslo je do greske")
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