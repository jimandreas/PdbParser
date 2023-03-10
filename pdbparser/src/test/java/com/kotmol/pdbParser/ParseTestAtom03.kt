/*
 *  Copyright 2020-2023 James Andreas
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

@file:Suppress(
    "unused",
    "unused_variable",
    "unused_parameter",
    "unused_property",
    "deprecation",
    "ConstantConditionIf",
    "LocalVariableName",
    "PropertyName")

package com.kotmol.pdbParser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

// https://blog.jetbrains.com/idea/2016/08/using-junit-5-in-intellij-idea/

internal class ParseTestAtom03 {

    lateinit var str : ByteArrayInputStream
    val mol = Molecule()
    val parse = ParserPdbFile(mol)

    @org.junit.jupiter.api.BeforeEach
    fun setUp() { // from 1bna.pdb
        val anAtom = """
ATOM      1  O5'  DC A   1      18.935  34.195  25.617  1.00 64.35           O  
ATOM      2  C5'  DC A   1      19.130  33.921  24.219  1.00 44.69           C  
ATOM      3  C4'  DC A   1      19.961  32.668  24.100  1.00 31.28           C 
        """.trimIndent()

        str = anAtom.byteInputStream()
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        str.close()
    }

    @Test
    @DisplayName( "test with 3 atoms - should have a bond list of size 2 as a result")
    fun testWithThreeAtoms() {

        var messages : MutableList<String> = mutableListOf()

        ParserPdbFile
                .Builder(mol)
                .loadPdbFromStream(str)
                .parse()

        assertEquals(3, mol.maxAtomNumber)

        val atoms = mol.atomNumberToAtomInfoHash
        assertEquals(3, atoms.size)

        val firstAtom = atoms[1]
        assertEquals("O5'", firstAtom?.atomName)

        assertEquals(2, mol.bondList.size)
    }
}