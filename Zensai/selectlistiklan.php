<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $q=mysqli_query($mysqli,"SELECT linkpiciklan, keterangan FROM mst_iklan WHERE tgl_turun >= CURDATE() AND status = 'approved'");
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