<?php
   header('content-type:application/json;charset=utf-8');
   include "conn.php";

   $q=mysqli_query($mysqli,"SELECT keterangan, linkpiciklan FROM mst_iklan WHERE tgl_turun >= CURDATE() AND status = 'approved' ORDER BY RAND() LIMIT 1;");
   $response=array();

   if(mysqli_num_rows($q)>0){
    $response["data"] = array();
    while($r = mysqli_fetch_array($q)){   
      $user = array();
      $user["keterangan"]=$r["keterangan"];
      $user["linkpiciklan"]=$r["linkpiciklan"];
      array_push($response["data"], $user);
    }
    $response["success"] = 1;
    $response["message"] = "Data berhasil dibaca";
    echo json_encode($response);
  } else {
    $response["success"] = 0;
    $response["message"] = "Tidak ada data";
    echo json_encode($response);
  }

     
?> 