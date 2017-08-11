/*
 * Copyright (C) 2017 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Black Duck Software ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Black Duck Software.
 */
package com.blackducksoftware.integration.hub.detect.bomtool.cpan

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import com.blackducksoftware.integration.hub.bdio.simple.model.DependencyNode
import com.blackducksoftware.integration.hub.detect.nameversion.NameVersionNodeTransformer
import com.blackducksoftware.integration.hub.detect.testutils.TestUtil

class CpanPackagerTest {
    private final TestUtil testUtil = new TestUtil()
    private final CpanPackager cpanPackager = new CpanPackager()

    private final String cpanListText = testUtil.getResourceAsUTF8String('cpan/cpanList.txt')
    private final String showDepsText = testUtil.getResourceAsUTF8String('cpan/showDeps.txt')

    @Before
    public void init() {
        cpanPackager.cpanListParser = new CpanListParser()
        cpanPackager.nameVersionNodeTransformer = new NameVersionNodeTransformer()
    }

    @Test
    public void getDirectModuleNamesTest() {
        List<String> names = cpanPackager.getDirectModuleNames(showDepsText)
        assertEquals(4, names.size())
        assertTrue(names.contains('ExtUtils::MakeMaker'))
        assertTrue(names.contains('Test::More'))
        assertTrue(names.contains('perl'))
        assertTrue(names.contains('ExtUtils::MakeMaker'))
    }

    @Test
    public void makeDependencyNodesTest() {
        Set<DependencyNode> dependencyNodes = cpanPackager.makeDependencyNodes(cpanListText, showDepsText)
        testUtil.testJsonResource('cpan/expectedDependencyNodes.json', dependencyNodes)
    }
}