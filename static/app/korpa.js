Vue.component("shopping-cart", {
	data: function () {
		    return {
		    	cart: {},
				total: 0.0
		    }
	},
	template: ` 
<div>
		<h1>Moja korpa:</h1>
		
		<table border=1>
			<tr bgcolor="lightgrey">
				<th>Manifestacija</th>
				<th>Kolicina</th>
				<th>Tip karte</th>
				<th>Cijena</th>
			</tr>
	
			<tr v-for="item in this.cart">
				<td>{{item.naziv}}</td>
				<td>{{item.kolicina}}</td>
				<td>{{item.tipKarte}}</td>
				<td>{{item.cijena}}</td>
				<td><input type="button" value="Obrisi" v-on:click="obrisi(item)" /></td>
			</tr>
		</table>
		<br/>
		<h4> Total {{this.total}} </h4>
		<input type="button" value="Potvrdi rezervaciju!" v-on:click="odobri()" v-bind:disabled="this.total == 0.0" />
		<br/>
</div>		  
`
	, 
	methods : { 
		odobri : function () {
			let self = this;
			$.get("/rest/tickets/checkout", function(data){
				toast("Uspesno kupljeno.");
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