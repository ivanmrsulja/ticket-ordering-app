Vue.component("prikaz-pojedinacne", {
	data: function () {
		    return {
		      	manifestacija: {},
				user: null,
				commentable: false,
				rating: 1.0
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
				<td> <input type="button" value="Rezervisi!" v-on:click="addToCart()" v-bind:disabled="this.user == null || this.manifestacija.status == 'NEAKTIVNO' || this.manifestacija.datumOdrzavanja <= (Date.now()) || this.manifestacija.brojMesta == 0" /> </td>
			</tr>
		</table>
		
		<br/>
		<h2>Ostavi komentar:</h2>
	    <textarea class="comment" name="komentar" placeholder="Unesite komentar" v-bind:disabled="!this.commentable"></textarea>
	    <br/>
	    <form class="rating">
		  <label>
		    <input type="radio" name="stars" value="1" v-bind:disabled="!this.commentable" />
		    <span class="icon">★</span>
		  </label>
		  <label>
		    <input type="radio" name="stars" value="2" v-bind:disabled="!this.commentable" />
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		  </label>
		  <label>
		    <input type="radio" name="stars" value="3" v-bind:disabled="!this.commentable" />
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		    <span class="icon">★</span>   
		  </label>
		  <label>
		    <input type="radio" name="stars" value="4" v-bind:disabled="!this.commentable" />
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		  </label>
		  <label>
		    <input type="radio" name="stars" value="5" v-bind:disabled="!this.commentable" />
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		    <span class="icon">★</span>
		  </label>
		</form>
		<br/>
	    <input type="button" name="submit" value="Postavi" v-bind:disabled="!this.commentable" v-on:click="postComment()" >
		
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
				toast(data);
			})
			
		},
		postComment : function(){
			let com = {tekst: $("textarea[name=komentar]").val(), ocena: this.rating};
			
			$.ajax({
			url: "/rest/comments/postComment",
			data: JSON.stringify(com),
			method: "POST",
			contentType: "application/json",
			success: function(response){
				toast(response);
			},
			error: function(response){
				toast("Doslo je do greske.");
			}
			});
			
		}
	},
	mounted () {
        let self = this;
		$.get("/rest/manifestations/getCurrent", function(data){
			self.manifestacija = data;
		});
		$.get("/rest/users/currentUser", function(data){
			self.user = data;
		});
		$.get("/rest/manifestations/commentable", function(data){
			self.commentable = data;
		});
		$(":radio").change(function(){
			self.rating = this.value;
		});
    }
});