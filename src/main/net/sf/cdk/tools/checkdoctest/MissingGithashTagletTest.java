/* Copyright (C) 2009  Egon Willighagen <egonw@users.sf.net>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package net.sf.cdk.tools.checkdoctest;

import java.util.ArrayList;
import java.util.List;

import com.github.ojdcheck.test.IClassDocTester;
import com.github.ojdcheck.test.ITestReport;
import com.github.ojdcheck.test.TestReport;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Tag;

public class MissingGithashTagletTest implements IClassDocTester {

    public String getDescription() {
        return "Tests if the @cdk.githash tag is given.";
    }

    public String getName() {
        return "CDK Githash Taglet Test";
    }

    public List<ITestReport> test(ClassDoc classDoc) {
        List<ITestReport> reports = new ArrayList<ITestReport>();
        Tag[] tags = classDoc.tags("cdk.githash");
        if (tags.length == 0) {
            reports.add(
                new TestReport(
                    this, classDoc,
                    "Missing @cdk.githash tag.",
                    classDoc.position().line(), null
                )
            );
        }
        return reports;
    }

    public Priority getPriority() {
        return Priority.MINOR_ERROR;
    }

}
