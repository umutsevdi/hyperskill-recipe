<h5 id="description">Description</h5>

<p>Imagine a service that supports the registration process, can handle a lot of users, and each of them can add their own recipes. Also, a user can update or delete only their recipes but can view recipes added by other users. In this stage, you will implement all this functionality with Spring Boot Security.</p>

<p>The stage is divided into 3 steps. In the first step, you need to add an endpoint responsible for the user registration. The endpoint receives 2 fields: <code class="language-json">email</code> and <code class="language-json">password</code>. The second step is to enable Spring Security and configure the access restrictions – only the registered users with the correct login and password should have the rights to use the service. After that, restrict the deletion and updating to the recipe author only.</p>

<h5 id="objectives">Objectives</h5>

<p>The service should contain all features from the previous stages. To complete the project, you need to add the following functionality:</p>

<ul>
	<li>New endpoint <code class="language-json">POST /api/register</code> receives a JSON object with two fields: <code class="language-json">email</code> (string), and <code class="language-json">password</code> (string). If a user with a specified email does not exist, the program saves (registers) the user in a database and responds with <code class="language-json">200 (Ok)</code>. If a user is already in the database, respond with the <code class="language-json">400 (Bad Request)</code> status code. Both fields are <strong>required</strong> and must be <strong>valid</strong>: <code class="language-json">email</code> should contain <code class="language-json">@</code> and <code class="language-json">.</code> symbols, <code class="language-json">password</code> should contain at least 8 characters and shouldn't be blank. If the fields do not meet these restrictions, the service should respond with <code class="language-json">400 (Bad Request)</code>. Also, do not forget to use an encoder before storing a password in a database. <code class="language-json">BCryptPasswordEncoder</code> is a good choice.</li>
	<li>Include the Spring Boot Security dependency and configure access to the endpoints – all implemented endpoints (except <code class="language-json">/api/register</code>) should be available only to the registered and then authenticated and authorized via HTTP Basic auth users. Otherwise, the server should respond with the <code class="language-json">401 (Unauthorized)</code> status code.</li>
	<li>Add additional restrictions – only an author of a recipe can delete or update a recipe. If a user is not the author of a recipe, but they try to carry out the actions mentioned above, the service should respond with the <code class="language-json">403 (Forbidden)</code> status code.</li>
</ul>

<p><div class="alert alert-warning">For testing purposes, <code class="language-json">POST</code><code class="language-json">/actuator/shutdown</code> should be available without authentication.</div></p>

<p><button
        class="btn-sm btn-outline-secondary"
        onclick="getElementById('hint-2793').style.display='inline'">
        Hint
      </button>
      <div id="hint-2793" style="display:none;">If you use Postman or any similar program for testing and receive <code class="language-json">403 (Forbidden)</code>, try to disable <strong>CSRF </strong>(Cross-Site Request Forgery) protection. You can disable this type of protection by calling the following methods – <code class="language-json">.csrf().disable()</code> on the <code class="language-json">HttpSecurity</code> instance that was obtained after overriding the <code class="language-json">configure</code> method.</p>

<p>If you use the H2 console, you need to unblock it by disabling <strong>CSRF</strong> and <strong>X-Frame-Options </strong>that prevents clickjacking attacks, by calling the following methods: <code class="language-json">.csrf().disable().headers().frameOptions().disable()</code> on the <code class="language-json">HttpSecurity</code> instance. Also, make sure that Spring Security does not block the H2 console URLs.</div></p>

<h5 id="examples">Examples</h5>

<p><strong>Example 1: </strong><code class="language-json">POST /api/recipe/new</code> request without authentication</p>

<pre><code class="language-json">{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}</code></pre>

<p>Status code: <code class="language-json">401 (Unauthorized)</code></p>

<p><strong>Example 2: </strong><code class="language-json">POST /api/register</code> request without authentication</p>

<pre><code class="language-json">{
   "email": "Cook_Programmer@somewhere.com",
   "password": "RecipeInBinary"
}</code></pre>

<p>Status code: <code class="language-json">200 (Ok)</code></p>

<p>Further <code class="language-json">POST /api/recipe/new</code> request with basic authentication; email (login): Cook_Programmer@somewhere.com, and password: RecipeInBinary</p>

<pre><code class="language-json">{
   "name": "Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}</code></pre>

<p>Response:</p>

<pre><code class="language-json">{
   "id": 1
}</code></pre>

<p>Further <code class="language-json">PUT /api/recipe/1</code> request with basic authentication; email (login): Cook_Programmer@somewhere.com, password: RecipeInBinary</p>

<pre><code class="language-json">{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}</code></pre>

<p>Status code: <code class="language-json">204 (No Content)</code></p>

<p>Further <code class="language-json">GET /api/recipe/1</code> request with basic authentication; email (login): Cook_Programmer@somewhere.com, password: RecipeInBinary</p>

<p>Response:</p>

<pre><code class="language-json">{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "date": "2020-01-02T12:11:25.034734",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}</code></pre>

<p><strong>Example 3: </strong><code class="language-json">POST /api/register</code> request without authentication</p>

<pre><code class="language-json">{
   "email": "CamelCaseRecipe@somewhere.com",
   "password": "C00k1es."
}</code></pre>

<p>Status code: <code class="language-json">200 (Ok)</code></p>

<p>Further response for the <code class="language-json">GET /api/recipe/1</code> request with basic authentication; email (login): CamelCaseRecipe@somewhere.com, password: C00k1es.</p>

<pre><code class="language-json">{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "date": "2020-01-02T12:11:25.034734",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}</code></pre>

<p>Further <code class="language-json">PUT /api/recipe/1</code> request with basic authentication; email (login): CamelCaseRecipe@somewhere.com, password: C00k1es.</p>

<pre><code class="language-json">{
   "name": "Warming Ginger Tea",
   "category": "beverage",
   "description": "Ginger tea is a warming drink for cool weather, ...",
   "ingredients": ["1 inch ginger root, minced", "1/2 lemon, juiced", "1/2 teaspoon manuka honey"],
   "directions": ["Place all ingredients in a mug and fill with warm water (not too hot so you keep the beneficial honey compounds in tact)", "Steep for 5-10 minutes", "Drink and enjoy"]
}</code></pre>

<p>Status code: <code class="language-json">403 (Forbidden)</code></p>

<p>Further <code class="language-json">DELETE /api/recipe/1</code> request with basic authentication; email (login): CamelCaseRecipe@somewhere.com, password: C00k1es.</p>

<p>Status code: <code class="language-json">403 (Forbidden)</code></p>