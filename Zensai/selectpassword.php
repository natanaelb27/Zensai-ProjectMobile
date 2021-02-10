<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $username = $_POST['username'];
   $q=mysqli_query($mysqli,"SELECT password FROM mst_userlogin JOIN mst_customer ON mst_userlogin.username = mst_customer.username WHERE mst_userlogin.username='$username'");
   $response=array();
   if (mysqli_num_rows($q)>0)
    {
     $response["data"]=array();
     while($r=mysqli_fetch_array($q))
     {
     $user=array();
      $user["password"]=$r["password"];
      array_push($response["data"],$user);
     }


     $response["success"]=1;
     $response["message"]="data berhasil diambil";
      echo json_encode($response);
   }
   else
   { 
     $response["success"]=-1;
     $response["message"]="data kosong";
     echo json_encode($response);

   }


	  
?>	