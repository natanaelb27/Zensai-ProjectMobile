<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $id_iklan = $_POST['id_iklan'];

   $q=mysqli_query($mysqli,"SELECT keterangan, linkpiciklan FROM mst_iklan WHERE id_iklan='$id_iklan'");
   $response=array();
   
   if (mysqli_num_rows($q)>0)
   {
     $response["data"]=array();
     while($r=mysqli_fetch_array($q))
     {
     $user=array();
      $user["linkpiciklan"]=$r["linkpiciklan"];
      $user["keterangan"]=$r["keterangan"];
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