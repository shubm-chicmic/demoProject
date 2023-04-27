 function getCookie(cname) {
   let name = cname + "=";
   let decodedCookie = decodeURIComponent(document.cookie);
   let ca = decodedCookie.split(';');
   for(let i = 0; i <ca.length; i++) {
     let c = ca[i];
     while (c.charAt(0) == ' ') {
       c = c.substring(1);
     }
     if (c.indexOf(name) == 0) {
       return c.substring(name.length, c.length);
     }
   }
   return "";
 }
    let Username = getCookie("UserDetail");
    Username = Username.replace("_","  ");
    let imageUrl = getCookie("UserImageUrl");
    console.log(Username+"/////");

   document.getElementById("username1").innerHTML = Username;
   document.getElementById("username2").innerHTML = Username;

   document.querySelector("#image").setAttribute("src", imageUrl);


