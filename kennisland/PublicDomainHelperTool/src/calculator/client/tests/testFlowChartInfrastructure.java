package calculator.client.tests;

import org.junit.Test;

import calculator.client.flowchartinfrastructure.Answer;
import calculator.client.flowchartinfrastructure.DoubleQuestion;
import calculator.client.flowchartinfrastructure.Question;
import calculator.client.flowchartinfrastructure.Result;
import calculator.client.flowchartinfrastructure.RootQuestion;
import calculator.client.flowchartinfrastructure.SingleQuestion;
import calculator.client.flowchartinfrastructure.multichoice;
import calculator.client.parser.Parser;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * @author mzeinstra
 * 
 */
public class testFlowChartInfrastructure extends GWTTestCase {
    private static final String CORRECTFORMEDXML = "<calculator><questions> <step nr=\"0\"> <type>multiplechoice</type> <question> <text>What type of work is this?</text> <information></information> <param>class.classList</param> </question> <answer> <value>First fixation of a film</value> <gotoNr>1</gotoNr> <information></information> </answer> <answer> <value>Phonogram</value> <gotoNr>19</gotoNr> <information></information> </answer> <answer> <value>Literary or Artistic Works</value> <gotoNr>24</gotoNr> <information></information> </answer> <answer> <value>Broadcast</value> <gotoNr>15</gotoNr> <information></information> </answer> <answer> <value>Performance</value> <gotoNr>16</gotoNr> <information></information> </answer> <answer> <value>Unoriginal Database</value> <gotoNr>8</gotoNr> <information></information> </answer> </step> <step nr=\"1\"> <type>double</type> <question> <text>When was this made public?</text> <information></information> <param>First fixation of a film.publication year</param> </question> <question> <text>When was this first printed?</text> <information></information> <param>First fixation of a film.first printed year</param> </question> <answer> <value>True</value> <gotoNr>2</gotoNr> </answer> <answer> <value>False</value> <gotoNr>5</gotoNr> </answer> <evaluate>Q1-Q2&lt;50</evaluate> </step> <step nr=\"2\"> <type>single</type> <question> <text>When was this made public?</text> <information></information> </question> <answer> <value>True</value> <gotoNr>3</gotoNr> </answer> <answer> <value>False</value> <gotoNr>4</gotoNr> </answer> <evaluate>NOW-Q1&gt;50</evaluate> </step> <step nr=\"3\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"4\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"5\"> <type>single</type> <question> <text>When was this first printed?</text> <information></information> <param>First fixation of a film.first printed year</param> </question> <answer> <value>True</value> <gotoNr>3</gotoNr> </answer> <answer> <value>False</value> <gotoNr>4</gotoNr> </answer> <evaluate>NOW-Q1&gt;50</evaluate> </step> <step nr=\"3\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"4\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"19\"> <type>multiplechoice</type> <question> <text>Was the phonogram published within 50 years of creation?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>21</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>20</gotoNr> <information></information> </answer> </step> <step nr=\"21\"> <type>multiplechoice</type> <question> <text>Was it communicated to the public within 50 years of creation?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>22</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>23</gotoNr> <information></information> </answer> </step> <step nr=\"22\"> <type>multiplechoice</type> <question> <text>Was it communicated to the public more than 50 years ago?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>3</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>4</gotoNr> <information></information> </answer> </step> <step nr=\"3\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"4\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"23\"> <type>multiplechoice</type> <question> <text>Was it created more than 50 years ago?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>3</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>4</gotoNr> <information></information> </answer> </step> <step nr=\"3\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"4\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"20\"> <type>multiplechoice</type> <question> <text>Was it published more than 50 years ago?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>3</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>4</gotoNr> <information></information> </answer> </step> <step nr=\"3\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"4\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"24\"> <type>multiplechoice</type> <question> <text>What type of work is this?</text> <information></information> </question> <answer> <value>Law, decree, ordinance</value> <gotoNr>25</gotoNr> <information></information> </answer> <answer> <value>Other</value> <gotoNr>26</gotoNr> <information></information> </answer> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"26\"> <type>multiplechoice</type> <question> <text>What type of author does this work have?</text> <information></information> </question> <answer> <value>Unknown</value> <gotoNr>32</gotoNr> <information></information> </answer> <answer> <value>Legal Entity</value> <gotoNr>27</gotoNr> <information></information> </answer> <answer> <value>Natural person</value> <gotoNr>37</gotoNr> <information></information> </answer> </step> <step nr=\"32\"> <type>result</type> <result> <text>Orphan work</text> <information></information> </result> </step> <step nr=\"27\"> <type>multiplechoice</type> <question> <text>What is the nationality or place of residence of the author?</text> <information></information> </question> <answer> <value>Non EEA</value> <gotoNr>28</gotoNr> <information></information> </answer> <answer> <value>in EEA</value> <gotoNr>29</gotoNr> <information></information> </answer> </step> <step nr=\"28\"> <type>multiplechoice</type> <question> <text>What is the country of origin of this work?</text> <information></information> </question> <answer> <value>Non Berne TRIPS WCT</value> <gotoNr>30</gotoNr> <information></information> </answer> <answer> <value>Berne TRIPS WCT</value> <gotoNr>44</gotoNr> <information></information> </answer> <answer> <value>EEA</value> <gotoNr>29</gotoNr> <information></information> </answer> </step> <step nr=\"30\"> <type>multiplechoice</type> <question> <text>What is nationality or place of residence of the author?</text> <information></information> </question> <answer> <value>Berne TRIPS WCT</value> <gotoNr>44</gotoNr> <information></information> </answer> <answer> <value>Non Berne TRIPS WCT</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"44\"> <type>result</type> <result> <text>Rule of shorter term applies</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"44\"> <type>result</type> <result> <text>Rule of shorter term applies</text> <information></information> </result> </step> <step nr=\"29\"> <type>multiplechoice</type> <question> <text>Has it been make available?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>33</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>34</gotoNr> <information></information> </answer> </step> <step nr=\"33\"> <type>multiplechoice</type> <question> <text>Was it published more than 70 years after creation?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>35</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>36</gotoNr> <information></information> </answer> </step> <step nr=\"35\"> <type>multiplechoice</type> <question> <text>Was the work published more than 70 years ago?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>31</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"31\"> <type>result</type> <result> <text>Still protected</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"36\"> <type>multiplechoice</type> <question> <text>Was it published more than 25 years ago?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>31</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"31\"> <type>result</type> <result> <text>Still protected</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"34\"> <type>multiplechoice</type> <question> <text>Was it created more than 70 years ago?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>31</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"31\"> <type>result</type> <result> <text>Still protected</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"29\"> <type>multiplechoice</type> <question> <text>Has it been make available?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>33</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>34</gotoNr> <information></information> </answer> </step> <step nr=\"33\"> <type>multiplechoice</type> <question> <text>Was it published more than 70 years after creation?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>35</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>36</gotoNr> <information></information> </answer> </step> <step nr=\"35\"> <type>multiplechoice</type> <question> <text>Was the work published more than 70 years ago?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>31</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"31\"> <type>result</type> <result> <text>Still protected</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"36\"> <type>multiplechoice</type> <question> <text>Was it published more than 25 years ago?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>31</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"31\"> <type>result</type> <result> <text>Still protected</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"34\"> <type>multiplechoice</type> <question> <text>Was it created more than 70 years ago?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>31</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"31\"> <type>result</type> <result> <text>Still protected</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"37\"> <type>multiplechoice</type> <question> <text>What is the nationality or place of residence of the author?</text> <information></information> </question> <answer> <value>EEA</value> <gotoNr>40</gotoNr> <information></information> </answer> <answer> <value>Non EEA</value> <gotoNr>38</gotoNr> <information></information> </answer> </step> <step nr=\"40\"> <type>multiplechoice</type> <question> <text>Has it been communicated to the public?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>42</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>41</gotoNr> <information></information> </answer> </step> <step nr=\"42\"> <type>multiplechoice</type> <question> <text>Was it communicated to the public within 70 years of the death of the last surviving author?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>43</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>41</gotoNr> <information></information> </answer> </step> <step nr=\"43\"> <type>multiplechoice</type> <question> <text>Was the work published more than 25 years ago?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>31</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"31\"> <type>result</type> <result> <text>Still protected</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"41\"> <type>multiplechoice</type> <question> <text>Did the last surviving author die more than 70 years ago?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>31</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"31\"> <type>result</type> <result> <text>Still protected</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"41\"> <type>multiplechoice</type> <question> <text>Did the last surviving author die more than 70 years ago?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>31</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"31\"> <type>result</type> <result> <text>Still protected</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"38\"> <type>multiplechoice</type> <question> <text>What is the country of origin of this work?</text> <information></information> </question> <answer> <value>Berne TRIPS WCT</value> <gotoNr>44</gotoNr> <information></information> </answer> <answer> <value>EEA</value> <gotoNr>40</gotoNr> <information></information> </answer> <answer> <value>Non Berne TRIPS WCT</value> <gotoNr>39</gotoNr> <information></information> </answer> </step> <step nr=\"44\"> <type>result</type> <result> <text>Rule of shorter term applies</text> <information></information> </result> </step> <step nr=\"40\"> <type>multiplechoice</type> <question> <text>Has it been communicated to the public?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>42</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>41</gotoNr> <information></information> </answer> </step> <step nr=\"42\"> <type>multiplechoice</type> <question> <text>Was it communicated to the public within 70 years of the death of the last surviving author?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>43</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>41</gotoNr> <information></information> </answer> </step> <step nr=\"43\"> <type>multiplechoice</type> <question> <text>Was the work published more than 25 years ago?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>31</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"31\"> <type>result</type> <result> <text>Still protected</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"41\"> <type>multiplechoice</type> <question> <text>Did the last surviving author die more than 70 years ago?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>31</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"31\"> <type>result</type> <result> <text>Still protected</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"41\"> <type>multiplechoice</type> <question> <text>Did the last surviving author die more than 70 years ago?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>31</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"31\"> <type>result</type> <result> <text>Still protected</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"39\"> <type>multiplechoice</type> <question> <text>What is nationality or place of residence of the author?</text> <information></information> </question> <answer> <value>Berne TRIPS WCT</value> <gotoNr>44</gotoNr> <information></information> </answer> <answer> <value>Non Berne TRIPS WCT</value> <gotoNr>25</gotoNr> <information></information> </answer> </step> <step nr=\"44\"> <type>result</type> <result> <text>Rule of shorter term applies</text> <information></information> </result> </step> <step nr=\"25\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"15\"> <type>multiplechoice</type> <question> <text>Was the programme first broadcast more than 50 years ago?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>3</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>4</gotoNr> <information></information> </answer> </step> <step nr=\"3\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"4\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"16\"> <type>multiplechoice</type> <question> <text>Was a fixation of the performance made public within 50 years from the performance?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>17</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>18</gotoNr> <information></information> </answer> </step> <step nr=\"17\"> <type>multiplechoice</type> <question> <text>Was the fixation of the performance made public more than 50 years ago?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>3</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>4</gotoNr> <information></information> </answer> </step> <step nr=\"3\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"4\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"18\"> <type>multiplechoice</type> <question> <text>Did the performance take place more than 50 years ago?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>3</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>4</gotoNr> <information></information> </answer> </step> <step nr=\"3\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"4\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"8\"> <type>multiplechoice</type> <question> <text>Is the right holder a company or firm?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>6</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>9</gotoNr> <information></information> </answer> </step> <step nr=\"6\"> <type>multiplechoice</type> <question> <text>Of which countries is the rights holder a national or resident?</text> <information></information> </question> <answer> <value>In EEA</value> <gotoNr>7</gotoNr> <information></information> </answer> <answer> <value>Not EEA</value> <gotoNr>13</gotoNr> <information></information> </answer> </step> <step nr=\"7\"> <type>multiplechoice</type> <question> <text>Was the database made available to the public after its completion or last substantial change?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>45</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>46</gotoNr> <information></information> </answer> </step> <step nr=\"45\"> <type>multiplechoice</type> <question> <text>Was the database made available to the public in the last 15 years?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>13</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>14</gotoNr> <information></information> </answer> </step> <step nr=\"13\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"14\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"46\"> <type>multiplechoice</type> <question> <text>Was the database completed or sustantially changed within the last 15 years?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>13</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>14</gotoNr> <information></information> </answer> </step> <step nr=\"13\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"14\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"13\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"9\"> <type>multiplechoice</type> <question> <text>According to the law of which country was it formed?</text> <information></information> </question> <answer> <value>in EEA</value> <gotoNr>10</gotoNr> <information></information> </answer> <answer> <value>Not in EEA</value> <gotoNr>11</gotoNr> <information></information> </answer> </step> <step nr=\"10\"> <type>multiplechoice</type> <question> <text>In which country does it have its registered office, central administration, or principal place of business</text> <information></information> </question> <answer> <value>In EEA</value> <gotoNr>7</gotoNr> <information></information> </answer> <answer> <value>Not EEA</value> <gotoNr>13</gotoNr> <information></information> </answer> </step> <step nr=\"7\"> <type>multiplechoice</type> <question> <text>Was the database made available to the public after its completion or last substantial change?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>45</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>46</gotoNr> <information></information> </answer> </step> <step nr=\"45\"> <type>multiplechoice</type> <question> <text>Was the database made available to the public in the last 15 years?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>13</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>14</gotoNr> <information></information> </answer> </step> <step nr=\"13\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"14\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"46\"> <type>multiplechoice</type> <question> <text>Was the database completed or sustantially changed within the last 15 years?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>13</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>14</gotoNr> <information></information> </answer> </step> <step nr=\"13\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"14\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"13\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"11\"> <type>multiplechoice</type> <question> <text>In which country does it have its registered office</text> <information></information> </question> <answer> <value>Not EEA</value> <gotoNr>13</gotoNr> <information></information> </answer> <answer> <value>In EEA</value> <gotoNr>12</gotoNr> <information></information> </answer> </step> <step nr=\"13\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"12\"> <type>multiplechoice</type> <question> <text>Are its operations genuinely linked on an ongoing basis with the economy of an EEA state?</text> <information></information> </question> <answer> <value>in EEA</value> <gotoNr>7</gotoNr> <information></information> </answer> <answer> <value>Not EEA</value> <gotoNr>13</gotoNr> <information></information> </answer> </step> <step nr=\"7\"> <type>multiplechoice</type> <question> <text>Was the database made available to the public after its completion or last substantial change?</text> <information></information> </question> <answer> <value>Yes</value> <gotoNr>45</gotoNr> <information></information> </answer> <answer> <value>No</value> <gotoNr>46</gotoNr> <information></information> </answer> </step> <step nr=\"45\"> <type>multiplechoice</type> <question> <text>Was the database made available to the public in the last 15 years?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>13</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>14</gotoNr> <information></information> </answer> </step> <step nr=\"13\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"14\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"46\"> <type>multiplechoice</type> <question> <text>Was the database completed or sustantially changed within the last 15 years?</text> <information></information> </question> <answer> <value>No</value> <gotoNr>13</gotoNr> <information></information> </answer> <answer> <value>Yes</value> <gotoNr>14</gotoNr> <information></information> </answer> </step> <step nr=\"13\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> <step nr=\"14\"> <type>result</type> <result> <text>Not Public Domain</text> <information></information> </result> </step> <step nr=\"13\"> <type>result</type> <result> <text>Public Domain</text> <information></information> </result> </step> </questions><dataSchema> <class> <name>class</name> <param> <name>classList</name> <option>First fixation of a film</option> <option>Phonogram</option> <option>Literary or Artistic Works</option> <option>Broadcast</option> <option>Performance</option> <option>Unoriginal Database</option> </param> </class> <class> <name>First fixation of a film</name> <param> <name>publication year</name> </param> <param> <name>first printed year</name> </param> </class> </dataSchema></calculator>";

    private static void checkAnswer(Answer tmpA) {
        assertFalse("Each answer needs a label", tmpA.getAnswer().isEmpty());
        assertTrue("Each answer needs a redirect", tmpA.getRedirect() != null);
    }

    private static void checkQuestion(Question tmpQ) {
        if (tmpQ.getClass().getName().equals(
                "calculator.client.flowchartinfrastructure.multichoice")) {
            testMultichoice((multichoice) tmpQ);
            for (int i = 0; i < tmpQ.getAnswers().size(); i++) {
                final Answer tmpA = tmpQ.getAnswers().get(i);
                checkAnswer(tmpA);
                checkQuestion(tmpA.getRedirect());
            }
        }
        if (tmpQ.getClass().getName().equals(
                "calculator.client.flowchartinfrastructure.DoubleQuestion")) {
            testDoubleQuestion((DoubleQuestion) tmpQ);
            for (int i = 0; i < tmpQ.getAnswers().size(); i++) {
                final Answer tmpA = tmpQ.getAnswers().get(i);
                checkAnswer(tmpA);
                checkQuestion(tmpA.getRedirect());
            }
        }
        if (tmpQ.getClass().getName().equals(
                "calculator.client.flowchartinfrastructure.SingleQuestion")) {
            testSingleQuestion((SingleQuestion) tmpQ);
            for (int i = 0; i < tmpQ.getAnswers().size(); i++) {
                final Answer tmpA = tmpQ.getAnswers().get(i);
                checkAnswer(tmpA);
                checkQuestion(tmpA.getRedirect());
            }
        }
        if (tmpQ.getClass().getName().equals(
                "calculator.client.flowchartinfrastructure.Result")) {
            testResult((Result) tmpQ);
            assertTrue("Results do not have answers", tmpQ.getAnswers() == null);
        }

    }

    @Test
    private static void testDoubleQuestion(DoubleQuestion tmpQ) {
        assertFalse("Double cannot be empty", tmpQ.getQuestion1().isEmpty());
        assertFalse("Double cannot be empty", tmpQ.getQuestion2().isEmpty());
        assertTrue("Double needs two possible answers", tmpQ.getAnswers()
                .size() == 2);
        assertFalse("Double needs a question number", tmpQ.getQuestionNr()
                .isEmpty());

    }

    private static void testMultichoice(final multichoice tmpQ) {
        assertFalse("Multichoice cannot be empty", tmpQ.getQuestion().isEmpty());
        assertTrue("Multichoice needs at least two possible answers", tmpQ
                .getAnswers().size() > 1);
        assertFalse("Multichoice needs a question number", tmpQ.getQuestionNr()
                .isEmpty());

    }

    private static void testResult(Result tmpQ) {
        assertFalse("Result cannot be empty", tmpQ.getQuestion().isEmpty());

    }

    /**
     * Test method for
     * {@link calculator.client.flowchartinfrastructure.RootQuestion#getRoot()}.
     */
    @Test
    public static final void testRootQuestion() {
        final Parser testParser = new Parser();
        // Correct Input
        try {
            testParser.parse(CORRECTFORMEDXML);
        } catch (final Exception e) {
            fail("Should not fail: " + e.getMessage());
        }
        assertTrue("A Rootquestion needs to be found.", RootQuestion
                .getInstance().getRoot() != null);
        final Question tmpQ = RootQuestion.getInstance().getRoot();
        assertTrue("Questions should have been detected",
                tmpQ.toXML().length() > 0);
        assertTrue("First Question check failed: " + tmpQ.getQuestion(), tmpQ
                .getQuestion().equals("What type of work is this?"));
        assertTrue("First Question should have answers", tmpQ.getAnswers()
                .size() > 0);
        assertFalse("Answers should have Questions", tmpQ.getAnswers().get(0)
                .getRedirect().getQuestion().isEmpty());

        // recursive function that check each question rigorously on minimal
        // requirements
        checkQuestion(tmpQ);
    }

    private static void testSingleQuestion(SingleQuestion tmpQ) {
        assertFalse("Single cannot be empty", tmpQ.getQuestion().isEmpty());
        assertTrue("Single needs two possible answers", tmpQ.getAnswers()
                .size() == 2);
        assertFalse("Single needs a question number", tmpQ.getQuestionNr()
                .isEmpty());

    }

    @Override
    public String getModuleName() {
        return "calculator.PDcalculator";
    }
}