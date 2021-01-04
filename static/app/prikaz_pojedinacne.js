Vue.component("prikaz-pojedinacne", {
	data: function () {
		    return {
		      	manifestacija: {},
				user: null
		    }
	},
	template: ` 
<div>
		<h1>{{this.manifestacija.naziv}}</h1>
		
		<img :src="this.manifestacija.slika"  height=300 width=500></img>
		
		<table>
			<tr><th colspan=5> Rezervisi karte! </th></tr>
			<tr> <td> Tip karte: </td>
				<td> 
					<select name="tip_karte" id="tip_karte" >
					  <option value="REGULAR">REGULAR</option>
					  <option value="VIP">VIP</option>
					  <option value="FAN_PIT">FAN_PIT</option>
					</select> 
				</td> 
				<td> Kolicina: </td>
				<td> <input type="number" name="kolicina" min="0" :max="this.manifestacija.brojMesta"/> </td>
				<td> <input type="button" value="Rezervisi!" v-on:click="addToCart()" v-bind:disabled="this.user == null || this.manifestacija.status == 'NEAKTIVNO'" /> </td>
			</tr>
		</table>
		
</div>		  
`
	, 
	methods : {
		addToCart : function() {
			let man = this.manifestacija.id;
			let tip = $('#tip_karte option:selected').val();
			let amount = $('input[name=kolicina]').val();
			
			let sci = {idManifestacije: man, naziv: this.manifestacija.naziv, kolicina: amount, tipKarte: tip};
			
			$.post("rest/tickets/addToCart", JSON.stringify(sci), function(data){
				alert(data);
			})
			
		},
	},
	mounted () {
        let self = this;
		$.get("/rest/manifestations/getCurrent", function(data){
			self.manifestacija = data;
		});
		$.get("/rest/users/currentUser", function(data){
			self.user = data;
		});
    }
});