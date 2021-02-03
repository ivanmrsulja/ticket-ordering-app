Vue.component("shopping-cart", {
	data: function () {
		    return {
		    	cart: {},
				total: 0.0
		    }
	},
	template: ` 
<div style="width:40%">
		<h1>Moja korpa:</h1>
		
		<table class="table table-hover" v-bind:hidden="Object.keys(this.cart).length == 0" >
			<tr bgcolor="lightgrey">
				<th>Manifestacija</th>
				<th>Kolicina</th>
				<th>Tip karte</th>
				<th>Cijena</th>
				<th>Akcija</th>
			</tr>
	
			<tr v-for="item in this.cart">
				<td>{{item.naziv}}</td>
				<td>{{item.kolicina}}</td>
				<td>{{item.tipKarte}}</td>
				<td>{{item.cijena}}</td>
				<td><input type="button" class="btn btn-danger" value="Obrisi" v-on:click="obrisi(item)" /></td>
			</tr>
		</table>
		<img src="../css/prazno.png" v-bind:hidden="Object.keys(this.cart).length != 0" />
		<h1 v-bind:hidden="Object.keys(this.cart).length != 0" > Korpa je prazna! Kupi nesto! </h1>
		<br/>
		<h4 v-bind:hidden="Object.keys(this.cart).length == 0" > Total {{this.total}} </h4>
		<input type="button" class="btn btn-success" value="Potvrdi rezervaciju!" v-on:click="odobri()" v-bind:hidden="this.total == 0.0 || Object.keys(this.cart).length == 0" />
		<br/>
</div>		  
`
	, 
	methods : { 
		odobri : function () {
			let self = this;
			$.get("/rest/tickets/checkout", function(data){
				alert("Uspesno kupljeno.");
				 $.get("/rest/tickets/getCart", function(data){
		        	for(d of data){
						self.total += d.cijena;
					}
		        	self.cart = data;
		        	
		        });
			});
		},
		obrisi: function(item){
			let self = this;
			$.get("/rest/tickets/removeFromCart/" + item.id, function(data){
				self.total = 0.0; 
	        	for(d of data){
					self.total += d.cijena;
				}
	        	self.cart = data;
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
		let self = this;
        $.get("/rest/tickets/getCart", function(data){
        	for(d of data){
				self.total += d.cijena;
			}
        	self.cart = data;
        	
        });
    }
});