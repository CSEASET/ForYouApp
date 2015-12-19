<?php
error_reporting(0);
//header("Content-type: text/plain");
include '../vendor/autoload.php';
$pageno = ((isset($_REQUEST['p'])) ? $_REQUEST['p'] : 60 );

$pageno = ((int) $pageno) -1;


$parser = new \Smalot\PdfParser\Parser();

$pdf = $parser -> parseFile('2Final Syllabus-Environment-3rd Semester4,5,6,7,8.pdf');
$pages = $pdf -> getPages();

$page = $pages[$pageno] -> getText();

preg_match_all("/ET\w{2}[-\s]+\d{3}/", $page, $matches);
$paperCode = preg_replace("/-/", '', trim($matches[0][0]));
if(file_exists("../papers/$paperCode.txt")) echo ("CAUTION: File already exists, abe bc h file, agle pe chal GAANDU");
preg_match_all("/Paper:\s*(\w\s?)*/", $page, $matches);
$paperTitle = trim(substr(trim($matches[0][0]), 6));

preg_match_all("/.*:/", $page, $matches);
$unitTitles = $matches[0];

preg_match_all("/.*/", $page, $matches);
$unitDetails = implode(PHP_EOL, $matches[0]);
//$HTMLfile = "temp/" . rand(1, 100000) . ".txt";
//file_put_contents($HTMLfile, $unitDetails);
?>

<html>
	<body><br />
		Check paper Code :
		<input type="text" id="paperCode" name="paperCode" onblur="check(this);" onfocus="check(this);" value="<?php echo $paperCode ?>"/>
		<select id="paperCredits">
		<option value="1">1</option>
		<option value="2">2</option>
		<option value="3" selected>3</option>
		<option value="4">4</option>
		</select>
		<br />
		<br />
		Check paper Title :
		<input type="text" id="paperTitle" name="paperTitle" onblur="check2(this);" onfocus="check2(this);" value="<?php echo $paperTitle ?>" />
		<br />
		<br />
		UNIT-I
		<br />
		<select name="unitTitles" onchange="check3(this,1)">
			<?php
			foreach ($unitTitles as $unitTitle) {
				$unitTitle = substr($unitTitle, 0, -1);
				echo "<option value=\"$unitTitle\">$unitTitle</option>";
			}
			?>
		</select>
		<input size="30" type="text" name="u1Title" id="u1Title"/>
		<br />
		Enter Unit Details :
		<br />
		<textarea name="u1Details" id="u1Details" rows="5" cols="150" onblur="check4(this)"></textarea>
		<br />
		UNIT-II
		<br />
		<select name="unitTitles" onchange="check3(this,2)">
			<?php
			foreach ($unitTitles as $unitTitle) {
				$unitTitle = substr($unitTitle, 0, -1);
				echo "<option value=\"$unitTitle\">$unitTitle</option>";
			}
			?>
		</select>
		<input size="30" type="text" name="u2Title" id="u2Title"/>
		<br />
		Enter Unit Details :
		<br />
		<textarea name="u2Details" id="u2Details" rows="5" cols="150" onblur="check4(this)"></textarea>
		<br />
		UNIT-III
		<br />
		<select name="unitTitles" onchange="check3(this,3)">
			<?php
			foreach ($unitTitles as $unitTitle) {
				$unitTitle = substr($unitTitle, 0, -1);
				echo "<option value=\"$unitTitle\">$unitTitle</option>";
			}
			?>
		</select>
		<input size="30" type="text" name="u3Title" id="u3Title"/>
		<br />
		Enter Unit Details :
		<br />
		<textarea name="u3Details" id="u3Details" rows="8" cols="150" onblur="check4(this)"></textarea>
		<br />
		UNIT-IV
		<br />
		<select name="unitTitles" onchange="check3(this,4)">
			<?php
			foreach ($unitTitles as $unitTitle) {
				$unitTitle = substr($unitTitle, 0, -1);
				echo "<option value=\"$unitTitle\">$unitTitle</option>";
			}
			?>
		</select>
		<input size="30" type="text" name="u4Title" id="u4Title"/>
		<br />
		Enter Unit Details :
		<br />
		<textarea name="u4Details" id="u4Details" rows="5" cols="150" onblur="check4(this)"></textarea>
		<br />
		<br />
		<textarea id="json" rows="5" cols="150"></textarea>
		<input type="submit" name="Submit!" onclick="submit();"/>
		<script src="../js/jquery-2.1.0.min.js"></script>
		<script src="../js/main.js"></script>

	</body>
</html