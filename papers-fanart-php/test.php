<?php 

header("Content-type: text/json");
echo "[";
if ($handle = opendir('./papers')) {
    while (false !== ($entry = readdir($handle))) {
        if ($entry != "." && $entry != "..") {
            // do here
			echo file_get_contents("./papers/$entry") . ",";
//if)
//echo ",";
//else
//break ;
        }
    }
    closedir($handle);
} 
echo "]";


?>