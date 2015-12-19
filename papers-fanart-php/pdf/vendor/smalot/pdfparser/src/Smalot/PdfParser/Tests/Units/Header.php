<?php

/**
 * @file
 *          This file is part of the PdfParser library.
 *
 * @author  Sébastien MALOT <sebastien@malot.fr>
 * @date    2013-08-08
 * @license GPL-2.0
 * @url     <https://github.com/smalot/pdfparser>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

namespace Smalot\PdfParser\Tests\Units;

use mageekguy\atoum;

/**
 * Class Header
 *
 * @package Smalot\PdfParser\Tests\Units
 */
class Header extends atoum\test
{
    public function testParse()
    {
        $document = new \Smalot\PdfParser\Document();

        $content  = '<</Type/Page/SubType/Text>>foo';
        $position = 0;
        $header   = \Smalot\PdfParser\Header::parse($content, $document, $position);

        $this->assert->object($header)->isInstanceOf('\Smalot\PdfParser\Header');
        $this->assert->integer($position)->isEqualTo(27);
        $this->assert->array($header->getElements())->hasSize(2);

        // No header to parse
        $this->assert->castToString($header->get('Type'))->isEqualTo('Page');
        $content  = 'foo';
        $position = 0;
        $header   = \Smalot\PdfParser\Header::parse($content, $document, $position);

        $this->assert->object($header)->isInstanceOf('\Smalot\PdfParser\Header');
        $this->assert->integer($position)->isEqualTo(0);
        $this->assert->array($header->getElements())->hasSize(0);

        $position = 0;
        $content  = "<</CreationDate(D:20100309184803+01'00')/Author(Utilisateur)/Creator(PScript5.dll Version 5.2.2)/Producer(Acrobat Distiller 7.0.5 \(Windows\))/ModDate(D:20100310104810+01'00')/Title(Microsoft Word - CLEMI.docx)>>";
        $header   = \Smalot\PdfParser\Header::parse($content, $document, $position);
        $this->assert->integer($position)->isEqualTo(212);

        $position = 0;
        $content  = '[5 0 R ] foo';
        $header   = \Smalot\PdfParser\Header::parse($content, $document, $position);
        $this->assert->integer($position)->isEqualTo(8);
        $this->assert->array($header->getElements())->hasSize(1);

//        var_dump($header);
//        die();
    }

    public function testGetElements()
    {
        $document = new \Smalot\PdfParser\Document();

        $content  = '<</Type/Page/Subtype/Text>>foo';
        $position = 0;
        $header   = \Smalot\PdfParser\Header::parse($content, $document, $position);

        $this->assert->array($elements = $header->getElements())->hasSize(2);
        $this->assert->object(current($elements))->isInstanceOf('\Smalot\PdfParser\Element\ElementName');

        $types = $header->getElementTypes();
        $this->assert->array($types);
        $this->assert->string($types['Type'])->isEqualTo('Smalot\PdfParser\Element\ElementName');
        $this->assert->string($types['Subtype'])->isEqualTo('Smalot\PdfParser\Element\ElementName');
    }

    public function testHas()
    {
        $document = new \Smalot\PdfParser\Document();

        $content  = '<</Type/Page/SubType/Text/Font 5 0 R>>foo';
        $position = 0;
        $header   = \Smalot\PdfParser\Header::parse($content, $document, $position);

        $this->assert->boolean($header->has('Type'))->isEqualTo(true);
        $this->assert->boolean($header->has('SubType'))->isEqualTo(true);
        $this->assert->boolean($header->has('Font'))->isEqualTo(true);
        $this->assert->boolean($header->has('Text'))->isEqualTo(false);
    }

    public function testGet()
    {
        $document = new \Smalot\PdfParser\Document();

        $content  = '<</Type/Page/SubType/Text/Font 5 0 R/Resources 8 0 R>>foo';
        $position = 0;
        $header   = \Smalot\PdfParser\Header::parse($content, $document, $position);
        $object   = new \Smalot\PdfParser\Page($document, $header);
        $document->setObjects(array('5_0' => $object));

        $this->assert->object($header->get('Type'))->isInstanceOf('\Smalot\PdfParser\Element\ElementName');
        $this->assert->object($header->get('SubType'))->isInstanceOf('\Smalot\PdfParser\Element\ElementName');
        $this->assert->object($header->get('Font'))->isInstanceOf('\Smalot\PdfParser\Page');
        $this->assert->object($header->get('Image'))->isInstanceOf('\Smalot\PdfParser\Element\ElementMissing');

        try {
            $resources = $header->get('Resources');
            $this->assert->boolean(true)->isEqualTo(false);
        } catch (\Exception $e) {
            $this->assert->exception($e)->hasMessage('Missing object reference #8_0.');
        }
    }

    public function testResolveXRef()
    {
        $document = new \Smalot\PdfParser\Document();
        $content  = '<</Type/Page/SubType/Text/Font 5 0 R/Resources 8 0 R>>foo';
        $position = 0;
        $header   = \Smalot\PdfParser\Header::parse($content, $document, $position);
        $object   = new \Smalot\PdfParser\Page($document, $header);
        $document->setObjects(array('5_0' => $object));

        $this->assert->object($header->get('Font'))->isInstanceOf('\Smalot\PdfParser\Object');

        try {
            $this->assert->object($header->get('Resources'))->isInstanceOf('\Smalot\PdfParser\Element\ElementMissing');
            $this->assert->boolean(true)->isEqualTo(false);
        } catch (\Exception $e) {
            $this->assert->exception($e)->hasMessage('Missing object reference #8_0.');
        }
    }
}
